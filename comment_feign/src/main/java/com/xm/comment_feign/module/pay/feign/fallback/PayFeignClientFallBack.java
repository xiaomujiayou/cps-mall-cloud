package com.xm.comment_feign.module.pay.feign.fallback;

import com.xm.comment_feign.module.pay.feign.PayFeignClient;
import com.xm.comment_serialize.module.pay.vo.WxPayOrderResultVo;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import org.springframework.stereotype.Component;

@Component
public class PayFeignClientFallBack implements PayFeignClient {

    @Override
    public WxPayOrderResultVo wxPay(SuBillToPayBo suBillToPayBo) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }
}
