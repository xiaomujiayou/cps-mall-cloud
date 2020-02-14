package com.xm.api_pay.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.xm.api_pay.service.WxPayApiService;
import com.xm.comment_serialize.module.pay.vo.WxPayOrderResultVo;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付接口
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private WxPayApiService wxPayApiService;
    @Autowired
    private WxPayService wxService;

    /**
     * 微信支付
     * @param suBillToPayBo
     * @return
     */
    @PostMapping("/wx")
    public WxPayOrderResultVo wxPay(@RequestBody SuBillToPayBo suBillToPayBo) throws WxPayException {
        return wxPayApiService.collection(suBillToPayBo);
    }

    @PostMapping("/wx/order/notify")
    public String wxPayNotify(@RequestBody String xmlData)  {
        try {
            final WxPayOrderNotifyResult notifyResult = wxService.parseOrderNotifyResult(xmlData);
            wxPayApiService.orderNotify(notifyResult);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            return WxPayNotifyResponse.success("成功");
        }
    }
}

