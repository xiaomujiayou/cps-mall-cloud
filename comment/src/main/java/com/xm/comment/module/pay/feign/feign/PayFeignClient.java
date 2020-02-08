package com.xm.comment.module.pay.feign.feign;

import com.xm.comment.config.FeignConfiguration;
import com.xm.comment.module.pay.feign.feign.fallback.PayFeignClientFallBack;
import com.xm.comment_serialize.module.pay.vo.WxPayOrderResultVo;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.form.GetUserInfoForm;
import com.xm.comment_utils.response.Msg;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "api-pay",fallback = PayFeignClientFallBack.class,configuration = FeignConfiguration.class)
public interface PayFeignClient {
    @PostMapping(value = "/pay/wx",consumes = "application/json")
    public Msg<WxPayOrderResultVo> wxPay(@RequestBody SuBillToPayBo suBillToPayBo);
}
