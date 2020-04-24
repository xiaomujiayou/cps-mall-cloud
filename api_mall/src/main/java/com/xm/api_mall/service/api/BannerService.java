package com.xm.api_mall.service.api;

import com.xm.comment_serialize.module.mall.form.BannerForm;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;

import java.util.List;

public interface BannerService {

    /**
     * 首页banner
     * @param bannerForm
     * @return
     * @throws Exception
     */
    List<SmBannerEntity> banner(BannerForm bannerForm) throws Exception;

    /**
     * 首页滑动列表
     * @param bannerForm
     * @return
     * @throws Exception
     */
    List<SmBannerEntity> option(BannerForm bannerForm) throws Exception;
}
