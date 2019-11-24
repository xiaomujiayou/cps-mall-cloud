package com.xm.api_mall.controller;

import com.xm.api_mall.service.BannerService;
import com.xm.comment.response.Msg;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.mall.constant.BannerTypeEnum;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import com.xm.comment_utils.enu.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping("/{type}")
    public Msg<List<SmBannerEntity>> banner(@PathVariable("type") Integer type) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<SmBannerEntity> smBannerEntities = bannerService.getBannerByType(EnumUtils.getEnum(BannerTypeEnum.class,"type",type));
        return R.sucess(smBannerEntities);
    }
}
