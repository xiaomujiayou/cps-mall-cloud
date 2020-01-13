package com.xm.cron_service.task;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.xm.comment_mq.config.OrderMqConfig;
import com.xm.comment_serialize.module.cron.entity.ScPddOrderSyncHistoryEntity;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.comment_utils.mybatis.PageUtils;
import com.xm.cron_service.mapper.ScPddOrderSyncHistoryMapper;
import com.xm.cron_service.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.orderbyhelper.OrderByHelper;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 同步拼多多订单
 */
@Slf4j
@Component
@EnableScheduling
public class PddOrderSyncTask{

//    @Autowired
//    private MallFeignClient mallFeignClient;
    @Autowired
    private ScPddOrderSyncHistoryMapper scPddOrderSyncHistoryMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Resource(name = "pddTaskService")
    private TaskService pddTaskService;

    //最大间隔时间
    private final int MAX_INTERVAL_TIME = 24 * 60 * 60;
    //查询时间往前推10分钟(避免因平台服务处理时间额漏单)
    private final int MOVE_FORWARD = -10 * 60;

    /**
     * 每分钟同步一次
     */
//    @Async("threadPool")
    @Scheduled(cron = "0/30 * * * * ?")
    public void dowork() {

        ScPddOrderSyncHistoryEntity lastHistory = getLastHistory();
        if(lastHistory.getTotalPage() != null && lastHistory.getPage() < lastHistory.getTotalPage()){
            //查询下页数据
            lastHistory.setPage(lastHistory.getPage() + 1);
        }else if(lastHistory.getTotalPage() != null && lastHistory.getPage() >= lastHistory.getTotalPage()) {
            //开始下一轮查询
            Date now = getTime();
            Date lastEndTime = DateUtil.offset(lastHistory.getEndUpdateTime(),DateField.SECOND,MOVE_FORWARD);
            lastHistory = defaultEntity();
            lastHistory.setStartUpdateTime(lastEndTime);
            lastHistory.setEndUpdateTime(now);
            if(DateUtil.between(now,lastEndTime, DateUnit.SECOND) >= MAX_INTERVAL_TIME){
                //查询时间段超时则重新计算结束时间
                lastHistory.setEndUpdateTime(DateUtil.offset(lastEndTime, DateField.SECOND,MAX_INTERVAL_TIME));
                log.info("拼多多订单服务 - 时间超限：距离上次查询时间超时 超时区间：[{}] - [{}] 已修正为：[{}] - [{}]",
                        lastEndTime,
                        DateUtil.format(now,"yyyy-MM-dd HH:mm:ss"),
                        lastHistory.getStartUpdateTime(),
                        lastHistory.getEndUpdateTime());
            }
        }
        PageBean<SuOrderEntity> pageBean = null;
        try {
            pageBean = pddTaskService.getOrderByIncrement(
                    lastHistory.getStartUpdateTime(),
                    lastHistory.getEndUpdateTime(),
                    lastHistory.getPage(),
                    lastHistory.getPageSize());
        } catch (Exception e) {
            log.error("拼多多订单服务 - 同步出错：[{}]", e);
        }

        if(pageBean.getList() != null){
            pageBean.getList().stream().forEach(o->{
                log.debug("拼多多订单服务 - 收到订单：[{}]", JSON.toJSONString(o));
                rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,o);
            });
        }
        lastHistory.setReturnCount((int)pageBean.getTotal());
        lastHistory.setTotalPage(PageUtils.calcTotalPage((int)pageBean.getTotal(),pageBean.getPageSize()));
        lastHistory.setCreateTime(new Date());
        lastHistory.setId(null);
        scPddOrderSyncHistoryMapper.insertSelective(lastHistory);
        log.debug("拼多多订单服务 end");
    }

    /**
     * 获取最后一条记录
     * @return
     */
    private ScPddOrderSyncHistoryEntity getLastHistory(){
        PageHelper.startPage(1,1);
        OrderByHelper.orderBy("create_time desc");
        List<ScPddOrderSyncHistoryEntity> historys = scPddOrderSyncHistoryMapper.selectAll();
        if(historys != null && historys.size() > 0)
            return historys.get(0);
        return defaultEntity();
    }

    /**
     * 获取默认配置
     * @return
     */
    private ScPddOrderSyncHistoryEntity defaultEntity(){
        ScPddOrderSyncHistoryEntity entity = new ScPddOrderSyncHistoryEntity();
        Date now = getTime();
        entity.setStartUpdateTime(new Date(now.getTime() - 30 * 1000));
        entity.setEndUpdateTime(now);
        entity.setPage(1);
        entity.setPageSize(40);
        return entity;
    }

    /**
     * 获取当前时间
     */

    private Date getTime(){
        Date date = null;
        try {
            date = pddTaskService.getTime();
        } catch (Exception e) {
            log.error("{}",e);
        }
        return date;
    }
}
