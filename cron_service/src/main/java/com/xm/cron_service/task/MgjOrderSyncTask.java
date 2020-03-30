package com.xm.cron_service.task;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.xm.comment_mq.message.config.OrderMqConfig;
import com.xm.comment_serialize.module.cron.entity.ScMgjOrderRecordEntity;
import com.xm.comment_serialize.module.cron.entity.ScMgjOrderSyncHistoryEntity;
import com.xm.comment_serialize.module.cron.entity.ScPddOrderSyncHistoryEntity;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.comment_utils.mybatis.PageUtils;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.cron_service.mapper.ScMgjOrderRecordMapper;
import com.xm.cron_service.mapper.ScMgjOrderSyncHistoryMapper;
import com.xm.cron_service.mapper.ScPddOrderSyncHistoryMapper;
import com.xm.cron_service.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 同步拼多多订单
 */
@Slf4j
@Component
@EnableScheduling
public class MgjOrderSyncTask {

    @Autowired
    private ScMgjOrderSyncHistoryMapper scMgjOrderSyncHistoryMapper;
    @Autowired
    private ScMgjOrderRecordMapper scMgjOrderRecordMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Resource(name = "mgjTaskService")
    private TaskService mgjTaskService;

    /**
     * 发现新订单
     * 此任务无法更新订单
     */
//    @Async("threadPool")
    @Scheduled(cron = "0/30 * * * * ?")
    public void getNewOrder() {
        log.debug("蘑菇街订单服务 start");
        Date yesterDay = DateUtil.yesterday();
        Date toDay = DateUtil.parse(DateUtil.today());
        List<SuOrderEntity> orderEntities = new ArrayList<>();
        getAll(yesterDay,toDay,1,40,orderEntities);
        orderEntities.stream().forEach(o->{
            ScMgjOrderRecordEntity scMgjOrderRecordEntity = new ScMgjOrderRecordEntity();
            scMgjOrderRecordEntity.setOrderNum(o.getOrderSn());
            int count = scMgjOrderRecordMapper.selectCount(scMgjOrderRecordEntity);
            if(count <= 0){
                scMgjOrderRecordEntity.setOrderNum(o.getOrderSn());
                scMgjOrderRecordEntity.setState(o.getState());
                scMgjOrderRecordEntity.setLastUpdate(new Date());
                scMgjOrderRecordEntity.setCreateTime(scMgjOrderRecordEntity.getLastUpdate());
                scMgjOrderRecordMapper.insertSelective(scMgjOrderRecordEntity);
            }
            log.debug("蘑菇街订单服务 - 收到订单：[{}]", JSON.toJSONString(o));
            rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,o);
        });
        ScMgjOrderSyncHistoryEntity entity = new ScMgjOrderSyncHistoryEntity();
        entity.setStartUpdateTime(yesterDay);
        entity.setEndUpdateTime(toDay);
        entity.setReturnCount(orderEntities.size());
        entity.setCreateTime(new Date());
        scMgjOrderSyncHistoryMapper.insertSelective(entity);
        log.debug("蘑菇街订单服务 end");
    }

    private void getAll(Date start,Date end,Integer pageNum,Integer pageSize,List<SuOrderEntity> orders){
        PageBean<SuOrderEntity> pageBean = null;
        try {
            pageBean = mgjTaskService.getOrderByIncrement(start,end,pageNum ,40);
            if(pageBean.getList() != null)
                orders.addAll(pageBean.getList());
            if(pageBean.getTotal() > (pageNum * pageSize)){
                //查询下一页
                Thread.sleep(500);
                getAll(start,end,pageNum + 1,pageSize,orders);
            }
        } catch (Exception e) {
            log.error("蘑菇街订单服务 - 同步出错：[{}]", e);
        }
    }

    @Scheduled(cron = "0/30 * * * * ?")
    public void updateOrder(){
        Integer pageNum = 1;
        Integer pageSize = 40;
        PageHelper.startPage(pageNum,pageSize);
        OrderByHelper.orderBy("last_update asc");
        Example example = new Example(ScMgjOrderRecordEntity.class);
        Date twoMonthAgo = DateUtil.offsetMonth(new Date(),-2);
        example.createCriteria().andGreaterThan("createTime",twoMonthAgo);
        List<ScMgjOrderRecordEntity> list = scMgjOrderRecordMapper.selectByExample(example);
        if(list != null)
            list.stream().forEach(o->{
                try {
                    update(o);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        PageBean<ScMgjOrderRecordEntity> pageBean = new PageBean<>(list);
        while (pageNum * pageSize < pageBean.getTotal()){
            pageNum = pageNum + 1;
            PageHelper.startPage(pageNum,pageSize);
            OrderByHelper.orderBy("create_time asc");
            example.createCriteria().andGreaterThan("createTime",twoMonthAgo);
            list = scMgjOrderRecordMapper.selectByExample(example);
            if(list != null)
                list.stream().forEach(o->{
                    try {
                        update(o);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            pageBean = new PageBean<>(list);
        }
    }

    private void update(ScMgjOrderRecordEntity scMgjOrderRecordEntity) throws Exception {
        SuOrderEntity order = mgjTaskService.getOrderByNum(scMgjOrderRecordEntity.getOrderNum());
        if(!scMgjOrderRecordEntity.getState().equals(order.getState())){
            scMgjOrderRecordEntity.setState(order.getState());
            rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,order);
        }
        scMgjOrderRecordEntity.setLastUpdate(new Date());
        scMgjOrderRecordMapper.updateByPrimaryKeySelective(scMgjOrderRecordEntity);
    }

}
