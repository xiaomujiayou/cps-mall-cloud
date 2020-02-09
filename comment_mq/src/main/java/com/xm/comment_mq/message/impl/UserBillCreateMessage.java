package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import lombok.Data;

@Data
public class UserBillCreateMessage extends AbsUserActionMessage {

    public UserBillCreateMessage(Integer userId, SuBillEntity suBillEntity) {
        super(userId);
        this.suBillEntity = suBillEntity;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.USER_BILL_CREATE;
    private SuBillEntity suBillEntity;
}
