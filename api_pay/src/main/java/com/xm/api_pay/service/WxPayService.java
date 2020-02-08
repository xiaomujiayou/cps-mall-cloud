package com.xm.api_pay.service;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;

import java.util.List;

/**
 * 微信支付服务
 */
public interface WxPayService {

    /**
     * 收款
     * @param suBillToPayBo
     * @return
     */
    public WxPayUnifiedOrderResult collection(SuBillToPayBo suBillToPayBo);

    /**
     * 付款
     * @param suBillEntities
     */
    public void payment(List<SuBillEntity> suBillEntities);
}
