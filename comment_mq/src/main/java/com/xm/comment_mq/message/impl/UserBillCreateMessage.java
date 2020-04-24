package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import lombok.Data;

@Data
public class UserBillCreateMessage extends AbsUserActionMessage {
    public UserBillCreateMessage() {}

    public UserBillCreateMessage(Integer userId, SuBillEntity suBillEntity,SuOrderEntity suOrderEntity) {
        super(userId);
        this.suBillEntity = suBillEntity;
        this.suOrderEntity = suOrderEntity;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.USER_BILL_CREATE;
    private SuBillEntity suBillEntity;
    //账单所属订单
    private SuOrderEntity suOrderEntity;
}
