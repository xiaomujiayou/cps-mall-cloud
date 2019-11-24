package com.xm.api_mall.service.impl;

import com.xm.api_mall.mapper.SmBannerMapper;
import com.xm.api_mall.service.BannerService;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.mall.constant.BannerTypeEnum;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.List;

@Service("bannerService")
public class BannerServiceImpl implements BannerService {
    @Autowired
    private SmBannerMapper smBannerMapper;

    @Override
    public List<SmBannerEntity> getBannerByType(BannerTypeEnum bannerTypeEnum){
        OrderByHelper.orderBy("sort asc");
        SmBannerEntity criteria = new SmBannerEntity();
        criteria.setType(bannerTypeEnum.getType());
        criteria.setType(criteria.getType());
        return smBannerMapper.select(criteria);
    }

}
