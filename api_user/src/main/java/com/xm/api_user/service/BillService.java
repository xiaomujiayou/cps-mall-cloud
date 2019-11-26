package com.xm.api_user.service;

import com.xm.comment_serialize.module.user.entity.SuOrderEntity;

public interface BillService {

    /**
     * 通过订单创建订单收益账单
     * @param order
     */
    public void createByOrder(SuOrderEntity order);

    /**
     * 订单达到成功条件
     * @param order
     */
    public void payOutOrderBill(SuOrderEntity order);


    /**
     * 订单达到失败条件
     * @param order
     */
    public void invalidOrderBill(SuOrderEntity order);



}
