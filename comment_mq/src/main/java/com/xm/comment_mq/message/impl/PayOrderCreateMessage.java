package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.pay.entity.SpWxOrderInEntity;
import com.xm.comment_serialize.module.pay.vo.WxPayOrderResultVo;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import lombok.Data;

@Data
public class PayOrderCreateMessage extends AbsUserActionMessage {

    public PayOrderCreateMessage(Integer userId, SuBillEntity suBillEntity, SpWxOrderInEntity spWxOrderInEntity, SuBillToPayBo suBillToPayBo, WxPayOrderResultVo wxPayOrderResultVo) {
        super(userId);
        this.suBillEntity = suBillEntity;
        this.spWxOrderInEntity = spWxOrderInEntity;
        this.suBillToPayBo = suBillToPayBo;
        this.wxPayOrderResultVo = wxPayOrderResultVo;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.PAY_ORDER_CRREATE;
    private SuBillEntity suBillEntity;
    private SpWxOrderInEntity spWxOrderInEntity;
    private SuBillToPayBo suBillToPayBo;
    private WxPayOrderResultVo wxPayOrderResultVo;

}
