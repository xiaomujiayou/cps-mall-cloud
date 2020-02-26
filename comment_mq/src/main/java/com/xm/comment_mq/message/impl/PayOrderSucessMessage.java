package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.pay.entity.SpWxOrderNotifyEntity;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import lombok.Data;

@Data
public class PayOrderSucessMessage extends AbsUserActionMessage {

    public PayOrderSucessMessage() {}


    public PayOrderSucessMessage(Integer userId, SuBillToPayBo suBillToPayBo, SpWxOrderNotifyEntity spWxOrderNotifyEntity) {
        super(userId);
        this.suBillToPayBo = suBillToPayBo;
        this.spWxOrderNotifyEntity = spWxOrderNotifyEntity;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.PAY_ORDER_NOTIFY_SUCESS;
    private SuBillToPayBo suBillToPayBo;
    private SpWxOrderNotifyEntity spWxOrderNotifyEntity;
}
