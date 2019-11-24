package com.xm.api_user.controller;

import com.xm.api_user.service.UserService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.response.Msg;
import com.xm.comment.response.MsgEnum;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.ex.RolePermissionEx;
import com.xm.comment_serialize.module.user.form.UpdateUserInfoForm;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.xm.comment_serialize.module.user.form.GetUserInfoForm;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController{

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public Object test(){

        Map<String,Object> result = new HashMap<>();
        result.put("test",1);
        return R.sucess(result);
    }

    /**
     * 获取用户信息
     * @return
     */
    @PostMapping("/info")
    public Msg<SuUserEntity> info(@RequestBody GetUserInfoForm getUserInfoForm) throws WxErrorException {
        if(StringUtils.isAllBlank(getUserInfoForm.getCode(),getUserInfoForm.getOpenId()) && getUserInfoForm.getUserId() == null)
            return R.error(MsgEnum.PARAM_VALID_ERROR);
        SuUserEntity userEntity = userService.getUserInfo(getUserInfoForm);
        return userEntity != null ? R.sucess(userEntity):R.error(MsgEnum.DATA_ALREADY_NOT_EXISTS);
    }

    /**
     * 更新用户信息
     * @return
     */
    @PostMapping("/update")
    public Msg<Object> info(@Valid @RequestBody UpdateUserInfoForm updateUserInfoForm, BindingResult bindingResult, @LoginUser Integer userId) {
        userService.updateUserInfo(userId,updateUserInfoForm);
        return R.sucess();
    }

    /**
     * 更新用户信息
     * @return
     */
    @GetMapping("/role")
    public Msg<List<RolePermissionEx>> role(@LoginUser Integer userId) {
        return R.sucess(userService.getUserRole(userId));
    }

    /**
     * 获取上级用户
     * @param userId
     * @param userType  :UserTyoeConstant
     * @return
     */
    @GetMapping("/superUser")
    public Object superUser(Integer userId, int userType) {
        return R.sucess(userService.getSuperUser(userId,userType));
    }

}
