package com.xm.api_lottery.service;

import com.xm.comment_serialize.module.pay.vo.WxPayOrderResultVo;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;

public interface PayService {

    /**
     * 购买道具 微信支付
     * 1 -> 生成用户账单
     * 2 -> 微信统一下单
     * @param suUserEntity
     * @param propSpecId
     * @param clientIp
     * @return
     */
    public WxPayOrderResultVo wxPay(SuUserEntity suUserEntity, Integer propSpecId, String clientIp);
}
