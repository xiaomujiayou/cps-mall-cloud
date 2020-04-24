package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.wind.entity.SwCreditBillBindRecordEntity;
import com.xm.comment_serialize.module.wind.entity.SwCreditBillConfEntity;
import com.xm.comment_serialize.module.wind.entity.SwCreditBillPayRecordEntity;
import com.xm.comment_serialize.module.wind.entity.SwCreditRecordEntity;
import lombok.Data;

@Data
public class UserBindCreditBillMessage extends AbsUserActionMessage {

    public UserBindCreditBillMessage(){}

    public UserBindCreditBillMessage(Integer userId,SwCreditBillBindRecordEntity swCreditBillBindRecordEntity, SuBillEntity suBillEntity, SwCreditRecordEntity swCreditRecordEntity, SwCreditBillConfEntity swCreditBillConfEntity, SwCreditBillPayRecordEntity swCreditBillPayRecordEntity) {
        super(userId);
        this.suBillEntity = suBillEntity;
        this.swCreditBillBindRecordEntity = swCreditBillBindRecordEntity;
        this.swCreditRecordEntity = swCreditRecordEntity;
        this.swCreditBillConfEntity = swCreditBillConfEntity;
        this.swCreditBillPayRecordEntity = swCreditBillPayRecordEntity;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.USER_BIND_CREDIT_BILL;

    private SuBillEntity suBillEntity;
    private SwCreditBillBindRecordEntity swCreditBillBindRecordEntity;
    private SwCreditRecordEntity swCreditRecordEntity;
    private SwCreditBillConfEntity swCreditBillConfEntity;
    private SwCreditBillPayRecordEntity swCreditBillPayRecordEntity;
}
