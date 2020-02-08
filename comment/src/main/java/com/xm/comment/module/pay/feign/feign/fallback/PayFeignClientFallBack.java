package com.xm.comment.module.pay.feign.feign.fallback;

import com.xm.comment.module.pay.feign.feign.PayFeignClient;
import com.xm.comment_serialize.module.pay.vo.WxPayOrderResultVo;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_utils.response.R;
import org.springframework.stereotype.Component;

@Component
public class PayFeignClientFallBack implements PayFeignClient {

    @Override
    public Msg<WxPayOrderResultVo> wxPay(SuBillToPayBo suBillToPayBo) {
        return R.error(MsgEnum.SERVICE_AVAILABLE);
    }
}
