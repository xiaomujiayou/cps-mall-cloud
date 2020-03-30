package com.xm.api_mall.service.api.impl.mgj;

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

@Service("mgjBannerService")
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
