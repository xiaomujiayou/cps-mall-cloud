package com.xm.api_pay.service;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.xm.comment_serialize.module.pay.vo.WxPayOrderResultVo;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;

import java.util.List;

/**
 * 微信支付服务
 */
public interface WxPayApiService {

    /**
     * 收款
     * @param suBillToPayBo
     * @return
     */
    public WxPayOrderResultVo collection(SuBillToPayBo suBillToPayBo) throws WxPayException;

    /**
     * 付款
     * @param suBillEntities
     */
    public void payment(List<SuBillEntity> suBillEntities);

    /**
     * 微信支付成功通知
     * @param notifyResult
     */
    public void orderNotify(WxPayOrderNotifyResult notifyResult);
}
