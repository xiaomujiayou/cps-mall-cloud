package com.xm.api_mall.controller;

import com.xm.api_mall.service.ConfigService;
import com.xm.comment.response.Msg;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.mall.constant.ConfigEnmu;
import com.xm.comment_serialize.module.mall.entity.SmConfigEntity;
import com.xm.comment_serialize.module.user.entity.SuConfigEntity;
import com.xm.comment_utils.enu.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/config")
public class ConfigController  {

    @Autowired
    private ConfigService configService;

    @GetMapping("/one")
    public Msg<SmConfigEntity> getOneConfig(Integer userId, String configName, Integer configType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return R.sucess(configService.getConfig(userId, EnumUtils.getEnum(ConfigEnmu.class,"name",configName),configType));
    }
}
