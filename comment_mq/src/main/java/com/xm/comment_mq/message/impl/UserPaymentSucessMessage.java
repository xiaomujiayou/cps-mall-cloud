package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.cron.entity.ScBillPayEntity;
import com.xm.comment_serialize.module.pay.entity.SpWxEntPayOrderInEntity;
import lombok.Data;

@Data
public class UserPaymentSucessMessage extends AbsUserActionMessage {
    public UserPaymentSucessMessage(){}
    public UserPaymentSucessMessage(Integer userId,ScBillPayEntity scBillPayEntity,SpWxEntPayOrderInEntity spWxEntPayOrderInEntity) {
        super(userId);
        this.scBillPayEntity = scBillPayEntity;
        this.spWxEntPayOrderInEntity = spWxEntPayOrderInEntity;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.USER_PAYMENT_SUCESS;
    private SpWxEntPayOrderInEntity spWxEntPayOrderInEntity;
    private ScBillPayEntity scBillPayEntity;
}
