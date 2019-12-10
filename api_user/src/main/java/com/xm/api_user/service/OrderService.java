package com.xm.api_user.service;

import com.xm.comment_serialize.module.user.dto.OrderBillDto;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_utils.mybatis.PageBean;

/**
 * 用户订单服务
 */
public interface OrderService {


    /**
     * 收到一条订单消息
     * @param order
     */
    public void receiveOrderMsg(SuOrderEntity order);

    /**
     * 新订单处理
     * 订单处理的核心逻辑
     * @param order
     */
    public void onOrderCreate(SuOrderEntity order);


    /**
     * 更新订单状态以及相关信息
     * @param newOrder
     * @param oldOrder
     */
    public void updateOrderState(SuOrderEntity newOrder,SuOrderEntity oldOrder);

    /**
     * 查询用户订单
     * @param userId
     * @param type          1:自购订单,2:分享订单
     * @param platformType
     * @param state
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageBean<OrderBillDto> getOrderBill(Integer userId,Integer type, Integer platformType,Integer state,Integer pageNum,Integer pageSize);
}
