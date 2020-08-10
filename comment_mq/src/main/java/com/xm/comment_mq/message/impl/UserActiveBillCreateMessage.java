package com.xm.comment_mq.message.impl;


import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.active.entity.SaBillEntity;
import lombok.Data;

@Data
public class UserActiveBillCreateMessage extends AbsUserActionMessage {

    public UserActiveBillCreateMessage() {}

    public UserActiveBillCreateMessage(Integer userId, SaBillEntity saBillEntity) {
        super(userId);
        this.saBillEntity = saBillEntity;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.USER_ACTIVE_BILL_CREATE;
    private SaBillEntity saBillEntity;
}
