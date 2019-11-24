package com.xm.api_mall.service;

import com.xm.comment_serialize.module.mall.constant.BannerTypeEnum;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;

import java.util.List;

public interface BannerService {
    List<SmBannerEntity> getBannerByType(BannerTypeEnum bannerTypeEnum);
}
