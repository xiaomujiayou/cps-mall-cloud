package com.xm.cron_service.task;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.xm.comment.exception.GlobleException;
import com.xm.comment.module.mall.feign.MallFeignClient;
import com.xm.comment.response.Msg;
import com.xm.comment.response.MsgEnum;
import com.xm.comment_mq.config.OrderMqConfig;
import com.xm.comment_serialize.module.cron.entity.ScPddOrderSyncHistoryEntity;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.comment_utils.mybatis.PageUtils;
import com.xm.cron_service.mapper.ScPddOrderSyncHistoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.lang.ref.PhantomReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 同步拼多多订单
 */
@Slf4j
@Component
@EnableScheduling
public class PddOrderSyncTask {

    @Autowired
    private MallFeignClient mallFeignClient;
    @Autowired
    private ScPddOrderSyncHistoryMapper scPddOrderSyncHistoryMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 每分钟同步一次
     */
//    @Async("threadPool")
    @Scheduled(cron = "0/30 * * * * ?")
    public void dowork() throws InterruptedException {
       log.debug("拼多多订单服务 start");
        ScPddOrderSyncHistoryEntity lastHistory = getLastHistory();
        if(lastHistory.getTotalPage() != null && lastHistory.getPage() < lastHistory.getTotalPage()){
            //查询下页数据
            lastHistory.setPage(lastHistory.getPage() + 1);
        }else if(lastHistory.getTotalPage() != null && lastHistory.getPage() >= lastHistory.getTotalPage()) {
            //开始下一轮查询
            Date now = getTime();
            Date lastEndTime = lastHistory.getEndUpdateTime();
            lastHistory = defaultEntity();
            lastHistory.setStartUpdateTime(lastEndTime);
            lastHistory.setEndUpdateTime(now);
            if(lastEndTime.getTime() - now.getTime() >= 1000 * 60 * 60 * 24){
                //距离上次查询时间超过一天 重新开始
                lastHistory = defaultEntity();
                log.error("拼多多订单服务 - 同步失败：距离上次查询时间超过一天 无效区间：[{}] - [{}] 已修正为：[{}] - [{}]",
                        lastEndTime,
                        now,
                        lastHistory.getStartUpdateTime(),
                        lastHistory.getEndUpdateTime());
            }
        }
        Msg<PageBean<SuOrderEntity>> msg = mallFeignClient.getIncrement(
                lastHistory.getStartUpdateTime(),
                lastHistory.getEndUpdateTime(),
                lastHistory.getPage(),
                lastHistory.getPageSize());
        //接口出错
        if(!msg.getCode().equals(200)) {
            log.error("拼多多订单服务 - 同步出错：[{}]", msg.getMsg());
            return;
        }
        PageBean<SuOrderEntity> pageBean = msg.getData();

        if(pageBean.getList() != null){
            msg.getData().getList().stream().forEach(o->{
                log.debug("拼多多订单服务 - 收到订单：[{}]", JSON.toJSONString(o));
                rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,o);
            });
        }
        lastHistory.setReturnCount((int)msg.getData().getTotal());
        lastHistory.setTotalPage(PageUtils.calcTotalPage((int)msg.getData().getTotal(),msg.getData().getPageSize()));
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
        Msg<Date> msg = mallFeignClient.getTime();
        if(msg.getCode() != 200)
            throw new GlobleException(MsgEnum.SERVICE_AVAILABLE,"获取时间失败：" + msg.getMsg());
        return msg.getData();
    }


    
}
