package com.xm.api_user.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import com.sun.org.apache.xml.internal.utils.Hashtree2Node;
import com.xm.api_user.mapper.SuUserMapper;
import com.xm.api_user.service.UserService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.response.Msg;
import com.xm.comment.response.MsgEnum;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.ex.RolePermissionEx;
import com.xm.comment_serialize.module.user.form.UpdateUserInfoForm;
import com.xm.comment_serialize.module.user.vo.UserInfoVo;
import com.xm.comment_serialize.module.user.vo.UserProfitVo;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.xm.comment_serialize.module.user.form.GetUserInfoForm;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.GET;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController{

    @Autowired
    private UserService userService;
    @Autowired
    private SuUserMapper suUserMapper;

    /**
     * 获取用户信息摘要
     * @return/*-+
     */
    @PostMapping("/info")
    public Msg<SuUserEntity> info(@RequestBody GetUserInfoForm getUserInfoForm) throws WxErrorException {
        if(StringUtils.isAllBlank(getUserInfoForm.getCode(),getUserInfoForm.getOpenId()))
            return R.error(MsgEnum.PARAM_VALID_ERROR);
        try {
            SuUserEntity userEntity = userService.getUserInfo(getUserInfoForm);
            UserInfoVo userInfoVo = new UserInfoVo();
            BeanUtil.copyProperties(userEntity,userInfoVo);
            SuUserEntity result = new SuUserEntity();
            BeanUtil.copyProperties(userInfoVo,result);
            return userEntity != null ? R.sucess(result):R.error(MsgEnum.DATA_ALREADY_NOT_EXISTS);
        }catch (WxErrorException e){
            return R.error(MsgEnum.SYSTEM_INVALID_USER_ERROR);
        }
    }

    /**
     * 获取用户完整信息
     * @return
     * @throws WxErrorException
     */
    @GetMapping("/info/detail")
    public Msg<SuUserEntity> infoDetail(Integer userId) throws WxErrorException {
        return userId != null ? R.sucess(suUserMapper.selectByPrimaryKey(userId)):R.error(MsgEnum.DATA_ALREADY_NOT_EXISTS);
    }

    /**
     * 更新用户信息
     * @return
     */
    @PostMapping("/update")
    public Msg<UserInfoVo> info(@Valid @RequestBody UpdateUserInfoForm updateUserInfoForm, BindingResult bindingResult, @LoginUser Integer userId) throws WxErrorException {
        userService.updateUserInfo(userId,updateUserInfoForm);
        SuUserEntity userEntity = suUserMapper.selectByPrimaryKey(userId);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtil.copyProperties(userEntity,userInfoVo);
        return R.sucess(userInfoVo);
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

    /**
     * 用户收益概略信息
     * @param userId
     * @return
     */
    @GetMapping("/profit")
    public Msg getUserProft(@LoginUser Integer userId) {
        UserProfitVo userProfitVo = userService.getUserProft(userId);
        Map<String,String> result = new HashMap<>();
        result.put("totalCoupon",NumberUtil.roundStr(NumberUtil.div(Double.valueOf(userProfitVo.getTotalCoupon()).doubleValue() ,100d),2));
        result.put("totalCommission",NumberUtil.roundStr(NumberUtil.div(Double.valueOf(userProfitVo.getTotalCommission()).doubleValue() ,100d),2));
        result.put("todayProfit",NumberUtil.roundStr(NumberUtil.div(Double.valueOf(userProfitVo.getTodayProfit()).doubleValue() ,100d),2));
        result.put("totalConsumption",NumberUtil.roundStr(NumberUtil.div(Double.valueOf(userProfitVo.getTotalConsumption()).doubleValue() ,100d),2));
        result.put("totalShare",NumberUtil.roundStr(NumberUtil.div(Double.valueOf(userProfitVo.getTotalShare()).doubleValue() ,100d),2));
        result.put("totalProxyUser",userProfitVo.getTotalProxyUser().toString());
        return R.sucess(result);
    }

}
