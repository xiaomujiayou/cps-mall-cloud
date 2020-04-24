package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import lombok.Data;

@Data
public class UserMaliceCreditBillMessage extends AbsUserActionMessage {

    public UserMaliceCreditBillMessage() {}

    public UserMaliceCreditBillMessage(Integer userId, SuBillEntity suBillEntity,SuOrderEntity oldOrder,SuOrderEntity newOrder) {
        super(userId);
        this.suBillEntity = suBillEntity;
        this.oldOrder = oldOrder;
        this.newOrder = newOrder;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.USER_MALICE_CREDIT_BILL;
    private SuBillEntity suBillEntity;
    private SuOrderEntity oldOrder;
    private SuOrderEntity newOrder;
}
