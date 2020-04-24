package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_serialize.module.wind.entity.SwCreditRecordEntity;
import lombok.Data;

@Data
public class UserUnBindCreditBillMessage extends AbsUserActionMessage {

    public UserUnBindCreditBillMessage() {}

    public UserUnBindCreditBillMessage(Integer userId, SuBillEntity suBillEntity, SwCreditRecordEntity swCreditRecordEntity,Integer unBindType, String unBindReason) {
        super(userId);
        this.suBillEntity = suBillEntity;
        this.swCreditRecordEntity = swCreditRecordEntity;
        this.unBindType = unBindType;
        this.unBindReason = unBindReason;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.USER_UN_BIND_CREDIT_BILL;

    private SuBillEntity suBillEntity;
    private SwCreditRecordEntity swCreditRecordEntity;
    private Integer unBindType;
    private String unBindReason;
}
