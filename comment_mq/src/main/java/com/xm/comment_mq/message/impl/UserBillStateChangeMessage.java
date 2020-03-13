package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import lombok.Data;

@Data
public class UserBillStateChangeMessage extends AbsUserActionMessage {

    public UserBillStateChangeMessage() {}

    public UserBillStateChangeMessage(Integer userId, SuBillEntity oldBill, Integer newState, String failReason,SuUserEntity suUserEntity) {
        super(userId);
        this.oldBill = oldBill;
        this.newState = newState;
        this.failReason = failReason;
        this.suUserEntity = suUserEntity;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.USER_BILL_STATE_CHANGE;
    private SuBillEntity oldBill;
    private Integer newState;
    private String failReason;
    private SuUserEntity suUserEntity;

}
