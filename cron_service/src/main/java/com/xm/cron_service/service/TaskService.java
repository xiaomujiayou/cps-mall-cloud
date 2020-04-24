package com.xm.cron_service.service;

import com.alibaba.fastjson.JSONObject;
import com.mogujie.openapi.exceptions.ApiException;
import com.xm.comment_serialize.module.cron.bo.OrderWithResBo;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_utils.mybatis.PageBean;

import java.util.Date;
import java.util.List;

public interface TaskService {

    /**
     * 定时任务开始
     */
    public void start();

    /**
     * 按最后更新时间查询订单
     * @param startUpdateTime
     * @param endUpdateTime
     * @return
     */
    public PageBean<OrderWithResBo> getOrderByIncrement(Date startUpdateTime, Date endUpdateTime, Integer pageNum, Integer pageSize) throws Exception;

    /**
     * 根据单号查订单
     * 如果订单存在，则会重新投递
     * @param orderNum
     * @return
     */
    public List<OrderWithResBo> getOrderByNum(String orderNum) throws Exception;

    /**
     * 获取系统时间
     * @return
     */
    public Date getTime() throws Exception;
}
