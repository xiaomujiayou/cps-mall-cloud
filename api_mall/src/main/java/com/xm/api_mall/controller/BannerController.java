package com.xm.api_mall.controller;

import com.xm.api_mall.component.PlatformContext;
import com.xm.api_mall.service.BannerService;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_utils.response.R;
import com.xm.comment_serialize.module.mall.constant.BannerTypeEnum;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import com.xm.comment_utils.enu.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;
    @Autowired
    private PlatformContext productContext;

    /**
     * 获取banner
     * @param type
     * @return
     * @throws Exception
     */
    @GetMapping("/{type}")
    public Msg<List<SmBannerEntity>> banner(@PathVariable("type") Integer type) throws Exception {
        List<SmBannerEntity> smBannerEntities = bannerService.getBannerByType(EnumUtils.getEnum(BannerTypeEnum.class,"type",type));
        return R.sucess(smBannerEntities);
    }

    /**
     * 获取主题活动列表
     * @param platformType
     * @return
     * @throws Exception
     */
    @GetMapping("/theme")
    public Msg<List<SmBannerEntity>> themeBanner(Integer platformType) throws Exception {
        if(platformType == null)
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR);
        return R.sucess(productContext
                .platformType(platformType)
                .getService()
                .themes());
    }
}
