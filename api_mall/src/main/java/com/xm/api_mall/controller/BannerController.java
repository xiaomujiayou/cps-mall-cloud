package com.xm.api_mall.controller;

import com.xm.api_mall.service.api.BannerService;
import com.xm.comment.annotation.PlatformType;
import com.xm.comment_serialize.module.mall.form.BannerForm;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    /**
     * 获取首页banner列表
     */
    @GetMapping
    public List<SmBannerEntity> banner(@PlatformType BannerForm bannerForm) throws Exception {
        return bannerService.banner(bannerForm);
    }

    @GetMapping("/option")
    public List<SmBannerEntity> optBanner(@PlatformType BannerForm bannerForm) throws Exception {
        return bannerService.option(bannerForm);
    }
}
