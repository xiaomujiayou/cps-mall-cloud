package com.xm.api_pay.controller;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.xm.api_pay.service.WxPayService;
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
    private WxPayService wxPayService;

    /**
     * 微信支付
     * @param suBillToPayBo
     * @return
     */
    @PostMapping("/wx")
    public Msg<WxPayOrderResultVo> wxPay(@RequestBody SuBillToPayBo suBillToPayBo){
        WxPayUnifiedOrderResult wxPayUnifiedOrderResult = wxPayService.collection(suBillToPayBo);
        WxPayOrderResultVo wxPayOrderResultVo = new WxPayOrderResultVo();
        wxPayOrderResultVo.setPrepayId(wxPayUnifiedOrderResult.getPrepayId());
        return R.sucess(wxPayOrderResultVo);
    }
}

