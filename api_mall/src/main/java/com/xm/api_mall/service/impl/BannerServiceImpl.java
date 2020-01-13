package com.xm.api_mall.service.impl;

import com.xm.api_mall.mapper.SmBannerMapper;
import com.xm.api_mall.service.BannerService;
import com.xm.api_mall.service.ProductApiService;
import com.xm.comment_serialize.module.mall.constant.BannerTypeEnum;
import com.xm.comment_serialize.module.mall.constant.PlatformTypeConstant;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.orderbyhelper.OrderByHelper;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("bannerService")
public class BannerServiceImpl implements BannerService {
    @Autowired
    private SmBannerMapper smBannerMapper;

    @Resource(name = "pddProductApiService")
    private ProductApiService pddProductApiService;

    @Override
    public List<SmBannerEntity> getBannerByType(BannerTypeEnum bannerTypeEnum) throws Exception {
        OrderByHelper.orderBy("sort asc");
        SmBannerEntity criteria = new SmBannerEntity();
        criteria.setType(bannerTypeEnum.getType());
        criteria.setType(criteria.getType());
        List<SmBannerEntity> list = smBannerMapper.select(criteria);
        if(bannerTypeEnum.equals(BannerTypeEnum.HOME)){
            //添加拼多多主题商品活动
            List<SmBannerEntity> pddThemes = pddProductApiService.getThemeList().stream().map(o->{
                SmBannerEntity smBannerEntity = new SmBannerEntity();
                smBannerEntity.setId(o.getId());
                smBannerEntity.setType(bannerTypeEnum.getType());
                smBannerEntity.setImg(o.getImageUrl());
                smBannerEntity.setName(o.getName());
                smBannerEntity.setUrl(String.format("/pages/theme/index?themeId=%s&platformType=%s&themeName=%s&themeImg=%s",o.getId(),PlatformTypeConstant.PDD,o.getName(),o.getImageUrl()));
                smBannerEntity.setTarget(2);
                return smBannerEntity;
            }).collect(Collectors.toList());
            list.addAll(pddThemes);
        }

        return list;
    }

}
