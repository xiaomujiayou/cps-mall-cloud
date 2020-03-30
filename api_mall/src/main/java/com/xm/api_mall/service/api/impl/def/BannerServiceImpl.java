package com.xm.api_mall.service.api.impl.def;

import com.xm.api_mall.service.api.BannerService;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import com.xm.comment_serialize.module.mall.form.BannerForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bannerService")
public class BannerServiceImpl implements BannerService {
    @Override
    public List<SmBannerEntity> banner(BannerForm bannerForm) throws Exception {
        return null;
    }

    @Override
    public List<SmBannerEntity> option(BannerForm bannerForm) throws Exception {
        return null;
    }
}
