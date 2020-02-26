package com.xm.api_lottery.controller;

import com.xm.api_lottery.service.PayService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment_serialize.module.pay.vo.WxPayOrderResultVo;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    public WxPayOrderResultVo wxPay(@LoginUser SuUserEntity suUserEntity, Integer propSpecId){
        if(propSpecId == null)
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"propId不可为空");
        return payService.wxPay(suUserEntity,propSpecId,"23.91.200.25");
    }
}
