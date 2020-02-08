package com.xm.api_lottery.controller;

import com.xm.api_lottery.service.PayService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.module.user.feign.UserFeignClient;
import com.xm.comment_serialize.module.pay.vo.WxPayOrderResultVo;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_utils.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buy")
public class PayController {

    @Autowired
    private PayService payService;

    /**
     * 购买道具
     * @param propSpecId
     * @return
     */
    @GetMapping("/wx")
    public Msg<WxPayOrderResultVo> wxPay(@LoginUser SuUserEntity suUserEntity, Integer propSpecId){
        if(propSpecId == null)
            return R.error(MsgEnum.PARAM_VALID_ERROR,"propId不可为空");
        return R.sucess(payService.wxPay(suUserEntity,propSpecId,""));
    }
}
