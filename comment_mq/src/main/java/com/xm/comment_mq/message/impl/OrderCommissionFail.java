package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import lombok.Data;

@Data
public class OrderCommissionFail extends AbsUserActionMessage {
    //订单信息
    private OrderInfo orderInfo;
    //相关账单
    private SuBillEntity suBillEntity;
    //失败原因
    private String failReason;
}
