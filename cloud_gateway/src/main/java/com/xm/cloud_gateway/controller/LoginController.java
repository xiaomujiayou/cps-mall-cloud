package com.xm.cloud_gateway.controller;

import cn.hutool.core.util.StrUtil;
import com.xm.cloud_gateway.shiro.token.ManageToken;
import com.xm.cloud_gateway.shiro.token.WeChatToken;
import com.xm.comment_feign.module.user.feign.UserFeignClient;
import com.xm.comment_serialize.module.user.form.AdminLoginForm;
import com.xm.comment_serialize.module.user.vo.SuAdminVo;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_utils.response.R;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.form.GetUserInfoForm;
import com.xm.comment_serialize.module.user.form.WechatLoginForm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private UserFeignClient userFeignClient;

    /**
     * 前台登录
     * @param wechatLoginForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/login")
    public Object login(@Valid @RequestBody WechatLoginForm wechatLoginForm, BindingResult bindingResult){
        GetUserInfoForm form = new GetUserInfoForm();
        form.setCode(wechatLoginForm.getCode());
        form.setShareUserId(wechatLoginForm.getShareUserId());
        SuUserEntity msg = null;
        try {
            msg = userFeignClient.getUserInfo(form);
        }catch (Exception e){
            e.printStackTrace();
            return R.error(MsgEnum.UNKNOWN_ERROR);
        }
        if(!SecurityUtils.getSubject().isAuthenticated()){
            WeChatToken token = new WeChatToken(msg.getOpenId());
            SecurityUtils.getSubject().login(token);
        }
        Map<String,Object> result = new HashMap<>();
        result.put("token",SecurityUtils.getSubject().getSession().getId());
        SuUserEntity userEntity = (SuUserEntity)SecurityUtils.getSubject().getPrincipal();
        Map<String,Object> userInfo = new HashMap<>();
        userInfo.put("nickname",userEntity.getNickname());
        userInfo.put("userSn",userEntity.getUserSn());
        userInfo.put("headImg",userEntity.getHeadImg());
        userInfo.put("id",userEntity.getId());
        userInfo.put("sex",userEntity.getSex());
        userInfo.put("state",userEntity.getState());
        result.put("userInfo",userInfo);
        return R.sucess(result);
    }

    /**
     * 后台登陆
     */
    @PostMapping("/manage/logout")
    public Object manageLogout(){
        if(SecurityUtils.getSubject().isAuthenticated()){
            SecurityUtils.getSubject().logout();
        }
        return R.sucess();
    }

    /**
     * 后台登录
     */
    @PostMapping("/manage/login")
    public Object manageLogin(@Valid @RequestBody AdminLoginForm adminLoginForm, BindingResult bindingResult){
        if(!SecurityUtils.getSubject().isAuthenticated()){
            ManageToken token = new ManageToken(adminLoginForm.getUserName(),adminLoginForm.getPassword(),true);
            try {
                SecurityUtils.getSubject().login(token);
            }catch (Exception e){
                return R.error(MsgEnum.SYSTEM_LOGIN_ERROR);
            }
        }
        ManageToken token = (ManageToken)SecurityUtils.getSubject().getPrincipal();
        SuAdminVo suAdminVo = token.getSuAdminVo();
        suAdminVo.setToken(SecurityUtils.getSubject().getSession().getId());
        return R.sucess(suAdminVo);
    }
}
