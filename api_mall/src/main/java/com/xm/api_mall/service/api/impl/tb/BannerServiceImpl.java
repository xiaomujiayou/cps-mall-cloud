package com.xm.api_mall.service.api.impl.tb;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.xm.api_mall.component.PddSdkComponent;
import com.xm.api_mall.mapper.SmBannerMapper;
import com.xm.api_mall.service.api.BannerService;
import com.xm.comment_serialize.module.mall.constant.AppTypeConstant;
import com.xm.comment_serialize.module.mall.constant.BannerTypeEnum;
import com.xm.comment_serialize.module.mall.constant.PlatformTypeConstant;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import com.xm.comment_serialize.module.mall.form.BannerForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("tbBannerService")
public class BannerServiceImpl implements BannerService {

    @Autowired
    private SmBannerMapper smBannerMapper;

    @Override
    public List<SmBannerEntity> banner(BannerForm bannerForm) throws Exception {
        return null;
    }

    @Override
    public List<SmBannerEntity> option(BannerForm bannerForm) throws Exception {
        OrderByHelper.orderBy("sort desc");
        SmBannerEntity criteria = new SmBannerEntity();
        criteria.setAppType(AppTypeConstant.WE_APP);
        criteria.setPlatformType(PlatformTypeConstant.TB);
        criteria.setDisable(1);
        criteria.setType(BannerTypeEnum.HOME_SLIDE_MUEN.getType());
        return smBannerMapper.select(criteria);
    }
}
