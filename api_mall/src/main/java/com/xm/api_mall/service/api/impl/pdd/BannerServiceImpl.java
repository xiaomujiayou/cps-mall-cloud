package com.xm.api_mall.service.api.impl.pdd;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xm.api_mall.component.PddSdkComponent;
import com.xm.api_mall.mapper.SmBannerMapper;
import com.xm.api_mall.service.api.BannerService;
import com.xm.comment.annotation.PlatformType;
import com.xm.comment_serialize.module.mall.constant.AppTypeConstant;
import com.xm.comment_serialize.module.mall.form.BannerForm;
import com.xm.comment_serialize.module.mall.constant.BannerTypeEnum;
import com.xm.comment_serialize.module.mall.constant.PlatformTypeConstant;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("pddBannerService")
public class BannerServiceImpl implements BannerService {

    @Autowired
    private PddSdkComponent pddSdkComponent;
    @Autowired
    private SmBannerMapper smBannerMapper;

    @Override
    public List<SmBannerEntity> banner(BannerForm bannerForm) throws Exception {
        OrderByHelper.orderBy("sort desc");
        SmBannerEntity criteria = new SmBannerEntity();
        criteria.setType(BannerTypeEnum.HOME.getType());
        criteria.setDisable(1);
        criteria.setAppType(AppTypeConstant.WE_APP);
        criteria.setPlatformType(PlatformTypeConstant.PDD);
        List<SmBannerEntity> list = smBannerMapper.select(criteria);
        if(list == null)
            list = new ArrayList<>();
            //添加拼多多主题商品活动
        List<SmBannerEntity> pddThemes = pddSdkComponent.getThemeList().stream().map(o->{
            if(StrUtil.isBlank(o.getImageUrl()))
                return null;
            SmBannerEntity smBannerEntity = new SmBannerEntity();
            smBannerEntity.setId(o.getId());
            smBannerEntity.setType(BannerTypeEnum.HOME.getType());
            smBannerEntity.setImg(o.getImageUrl());
            smBannerEntity.setName(o.getName());
            smBannerEntity.setUrl(String.format("/pages/theme/index?themeId=%s&platformType=%s&themeName=%s&themeImg=%s",o.getId(), PlatformTypeConstant.PDD,o.getName(),o.getImageUrl()));
            smBannerEntity.setTarget(2);
            return smBannerEntity;
        }).collect(Collectors.toList());
//        if(pddThemes.size() > 5)
//            pddThemes = pddThemes.subList(0,4);
        list.addAll(CollUtil.removeNull(pddThemes));
        return list;
    }

    @Override
    public List<SmBannerEntity> option(BannerForm bannerForm) throws Exception {
        OrderByHelper.orderBy("sort desc");
        SmBannerEntity criteria = new SmBannerEntity();
        criteria.setAppType(AppTypeConstant.WE_APP);
        criteria.setPlatformType(PlatformTypeConstant.PDD);
        criteria.setDisable(1);
        criteria.setType(BannerTypeEnum.HOME_SLIDE_MUEN.getType());
        return smBannerMapper.select(criteria);
    }
}
