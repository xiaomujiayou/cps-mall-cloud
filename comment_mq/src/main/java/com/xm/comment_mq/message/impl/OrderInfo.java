package com.xm.comment_mq.message.impl;

import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import lombok.Data;

@Data
public class OrderInfo {
    //订单类型(1:自购,2:代理购买,3:分享被购)
    private Integer orderType;
    //下单用户id(orderType = 2/3)
    private Integer buyUserId;
    //代理级别（orderType = 2）
    private Integer proxyLevel;
    //相关订单
    private SuOrderEntity suOrderEntity;
}
