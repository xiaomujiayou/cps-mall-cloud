package com.xm.api_user.service;

import com.xm.comment_serialize.module.user.entity.SuOrderEntity;

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
}
