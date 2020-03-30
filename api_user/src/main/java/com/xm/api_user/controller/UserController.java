package com.xm.api_user.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.xm.api_user.mapper.SuUserMapper;
import com.xm.api_user.service.SummaryService;
import com.xm.api_user.service.UserService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment_serialize.module.user.bo.UserProfitBo;
import com.xm.comment_serialize.module.user.entity.SuSummaryEntity;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.number.NumberUtils;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_utils.response.R;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.ex.RolePermissionEx;
import com.xm.comment_serialize.module.user.form.UpdateUserInfoForm;
import com.xm.comment_serialize.module.user.vo.UserInfoVo;
import com.xm.comment_serialize.module.user.vo.UserProfitVo;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.xm.comment_serialize.module.user.form.GetUserInfoForm;

import javax.validation.Valid;
import java.util.ArrayList;
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
    @Autowired
    private SummaryService summaryService;

    /**
     * 获取用户信息摘要
     * @return/*-+
     */
    @PostMapping("/info")
    public SuUserEntity info(@RequestBody GetUserInfoForm getUserInfoForm) throws WxErrorException {
        if(StringUtils.isAllBlank(getUserInfoForm.getCode(),getUserInfoForm.getOpenId()))
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR);
        try {
            SuUserEntity userEntity = userService.getUserInfo(getUserInfoForm);
            UserInfoVo userInfoVo = new UserInfoVo();
            BeanUtil.copyProperties(userEntity,userInfoVo);
            SuUserEntity result = new SuUserEntity();
            BeanUtil.copyProperties(userInfoVo,result);
            if(userEntity != null)
                return result;
            throw new GlobleException(MsgEnum.DATA_ALREADY_NOT_EXISTS);
        }catch (WxErrorException e){
            throw new GlobleException(MsgEnum.SYSTEM_INVALID_USER_ERROR);
        }
    }

    /**
     * 获取用户完整信息
     * @return
     * @throws WxErrorException
     */
    @GetMapping("/info/detail")
    public SuUserEntity infoDetail(Integer userId) throws WxErrorException {
        if(userId != null)
            return suUserMapper.selectByPrimaryKey(userId);
        throw new GlobleException(MsgEnum.DATA_ALREADY_NOT_EXISTS);

    }

    /**
     * 更新用户信息
     * @return
     */
    @PostMapping("/update")
    public UserInfoVo info(@Valid @RequestBody UpdateUserInfoForm updateUserInfoForm, BindingResult bindingResult, @LoginUser Integer userId) throws WxErrorException {
        userService.updateUserInfo(userId,updateUserInfoForm);
        SuUserEntity userEntity = suUserMapper.selectByPrimaryKey(userId);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtil.copyProperties(userEntity,userInfoVo);
        return userInfoVo;
    }

    /**
     * 更新用户信息
     * @return
     */
    @GetMapping("/role")
    public List<RolePermissionEx> role(@LoginUser Integer userId) {
        return userService.getUserRole(userId);
    }

    /**
     * 获取上级用户
     * @param userId
     * @param userType  :UserTyoeConstant
     * @return
     */
    @GetMapping("/superUser")
    public Object superUser(Integer userId, int userType) {
        return userService.getSuperUser(userId,userType);
    }

    /**
     * 用户收益概略信息
     * @param userId
     * @return
     */
    @GetMapping("/profit/list")
    public List<UserProfitVo> getUserProft(@LoginUser Integer userId) {
        SuSummaryEntity suSummaryEntity = summaryService.getUserSummary(userId);
        List<UserProfitVo> result = new ArrayList<>();
        result.add(new UserProfitVo("今日收益",DateUtil.today().equals(suSummaryEntity.getProfitTodayLastUpdate()) ? NumberUtils.fen2yuan(suSummaryEntity.getProfitToday()) : "0.00","/pages/my-bill/index"));
        result.add(new UserProfitVo("历史收益",NumberUtils.fen2yuan(suSummaryEntity.getProfitHistory()),"/pages/my-bill/index"));
        result.add(new UserProfitVo("等待确认",NumberUtils.fen2yuan(suSummaryEntity.getProfitWait()),"/pages/my-bill/index?type=0&state=1"));
        result.add(new UserProfitVo("准备发放",NumberUtils.fen2yuan(suSummaryEntity.getProfitReady()),"/pages/my-bill/index?type=0&state=2"));
        result.add(new UserProfitVo("锁定用户",suSummaryEntity.getTotalProxyUser() == null ? "0" : suSummaryEntity.getTotalProxyUser().toString(),"/pages/profit/profit"));
        result.add(new UserProfitVo("自购成交额",NumberUtils.fen2yuan(suSummaryEntity.getTotalBuy()),"/pages/order/order?type=0"));
        result.add(new UserProfitVo("分享成交额",NumberUtils.fen2yuan(suSummaryEntity.getTotalShare()),"/pages/order/order?type=1"));
        return result;
    }
    @GetMapping("/profit/desc")
    public Map<String,Object> getUserProft1(@LoginUser Integer userId) {
        SuSummaryEntity suSummaryEntity = summaryService.getUserSummary(userId);
        Map<String,Object> map = new HashMap<>();
        map.put("totalCoupon", NumberUtils.fen2yuan(suSummaryEntity.getTotalCoupon()));
        map.put("totalCommission",NumberUtils.fen2yuan(suSummaryEntity.getProfitCash()));
        return map;
    }



}
