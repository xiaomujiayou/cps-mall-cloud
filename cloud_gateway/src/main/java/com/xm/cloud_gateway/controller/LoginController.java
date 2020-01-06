package com.xm.cloud_gateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xm.cloud_gateway.shiro.token.WeChatToken;
import com.xm.comment.module.mall.feign.MallFeignClient;
import com.xm.comment.module.user.feign.UserFeignClient;
import com.xm.comment.response.Msg;
import com.xm.comment.response.MsgEnum;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.mall.constant.ConfigEnmu;
import com.xm.comment_serialize.module.mall.constant.ConfigTypeConstant;
import com.xm.comment_serialize.module.user.constant.UserTypeConstant;
import com.xm.comment_serialize.module.user.entity.SuPermissionEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.form.GetUserInfoForm;
import com.xm.comment_serialize.module.user.form.WechatLoginForm;
import com.xm.comment_serialize.module.user.vo.UserInfoVo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private UserFeignClient userFeignClient;

    @PostMapping("/login")
    public Object login(@Valid @RequestBody WechatLoginForm wechatLoginForm, BindingResult bindingResult){
        GetUserInfoForm form = new GetUserInfoForm();
        form.setCode(wechatLoginForm.getCode());
        form.setShareUserId(wechatLoginForm.getShareUserId());
        Msg<SuUserEntity> msg = userFeignClient.getUserInfo(form);
        WeChatToken token = new WeChatToken(msg.getData().getOpenId());
        if(!SecurityUtils.getSubject().isAuthenticated()){
            SecurityUtils.getSubject().login(token);
        }
        Map<String,Object> result = new HashMap<>();
        result.put("token",SecurityUtils.getSubject().getSession().getId());
        SuUserEntity userEntity = (SuUserEntity)SecurityUtils.getSubject().getPrincipal();
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("nickname",userEntity.getNickname());
        userInfo.put("headImg",userEntity.getHeadImg());
        userInfo.put("id",userEntity.getId());
        userInfo.put("sex",userEntity.getSex());
        userInfo.put("state",userEntity.getState());
        result.put("userInfo",userInfo);
        return R.sucess(result);
    }

}
