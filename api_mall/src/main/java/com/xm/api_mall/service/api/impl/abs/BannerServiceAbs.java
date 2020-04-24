package com.xm.api_mall.service.api.impl.abs;

import com.xm.api_mall.service.api.BannerService;
import com.xm.comment_serialize.module.mall.bo.ProductCriteriaBo;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import com.xm.comment_serialize.module.mall.form.*;
import org.springframework.stereotype.Service;

import java.util.List;

public abstract class BannerServiceAbs implements BannerService {

    @Override
    public List<SmBannerEntity> banner(BannerForm bannerForm) throws Exception {
        return null;
    }

    @Override
    public List<SmBannerEntity> option(BannerForm bannerForm) throws Exception {
        return null;
    }
}
