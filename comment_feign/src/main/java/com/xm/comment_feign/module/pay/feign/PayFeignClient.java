package com.xm.comment_feign.module.pay.feign;

import com.xm.comment_feign.module.pay.feign.fallback.PayFeignClientFallBack;
import com.xm.comment_serialize.module.pay.vo.WxPayOrderResultVo;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "api-pay",fallback = PayFeignClientFallBack.class)
public interface PayFeignClient {
    @PostMapping(value = "/pay/wx",consumes = "application/json")
    public WxPayOrderResultVo wxPay(@RequestBody SuBillToPayBo suBillToPayBo);
}
