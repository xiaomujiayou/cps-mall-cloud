package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import lombok.Data;

@Data
public class OrderSettlementFailMessage extends AbsUserActionMessage {

    public OrderSettlementFailMessage() {}

    public OrderSettlementFailMessage(Integer userId, SuOrderEntity suOrderEntity, String failReason) {
        super(userId);
        this.suOrderEntity = suOrderEntity;
        this.failReason = failReason;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.ORDER_SETTLEMENT_FAIL;
    //相关订单
    private SuOrderEntity suOrderEntity;
    //失败原因
    private String failReason;
}
