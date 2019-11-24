package com.xm.api_user.controller;

import com.xm.api_user.mapper.SuPidMapper;
import com.xm.api_user.service.UserService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.module.mall.feign.MallFeignClient;
import com.xm.comment.response.Msg;
import com.xm.comment.response.MsgEnum;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.user.entity.SuPidEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.ex.RolePermissionEx;
import com.xm.comment_serialize.module.user.form.GetUserInfoForm;
import com.xm.comment_serialize.module.user.form.UpdateUserInfoForm;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户推广位接口
 */
@RestController
@RequestMapping("/pid")
public class PidController {

    @Autowired
    private SuPidMapper suPidMapper;

    @Autowired
    private MallFeignClient mallFeignClient;

    @GetMapping
    public Msg<SuPidEntity> getPid(Integer userId,Integer platformType){
        SuPidEntity suPidEntity = new SuPidEntity();
        suPidEntity.setUserId(userId);
        suPidEntity.setPlatformType(platformType);
        suPidEntity = suPidMapper.selectOne(suPidEntity);
        if(suPidEntity != null && suPidEntity.getId() != null)
            return R.sucess(suPidEntity);
        String pid = mallFeignClient.generatePid(userId,platformType).getData();
        suPidEntity = new SuPidEntity();
        suPidEntity.setPlatformType(platformType);
        suPidEntity.setUserId(userId);
        suPidEntity.setPId(pid);
        suPidEntity.setCreateTime(new Date(System.currentTimeMillis()));
        suPidMapper.insertSelective(suPidEntity);
        return R.sucess(suPidEntity);
    }
}
