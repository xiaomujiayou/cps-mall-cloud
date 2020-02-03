package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import lombok.Data;

@Data
public class OrderSettlementSucess extends AbsUserActionMessage {
    //订单信息
    private OrderInfo orderInfo;
}
