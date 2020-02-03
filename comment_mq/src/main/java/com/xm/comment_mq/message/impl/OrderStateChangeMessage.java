package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_serialize.module.user.constant.OrderStateConstant;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import lombok.Data;

@Data
public class OrderStateChangeMessage extends AbsUserActionMessage {
    //订单信息
    private OrderInfo orderInfo;
    //新的状态(OrderStateContanst)
    private Integer newState;
}
