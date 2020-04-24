package com.xm.api_mall.service.api.impl.wph;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.xm.api_mall.component.PddSdkComponent;
import com.xm.api_mall.component.WphSdkComponent;
import com.xm.api_mall.service.ProductTestService;
import com.xm.api_mall.service.ProfitService;
import com.xm.api_mall.service.api.GoodsService;
import com.xm.api_mall.service.api.impl.abs.GoodsServiceAbs;
import com.xm.comment_serialize.module.mall.bo.ShareLinkBo;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.*;
import com.xm.comment_serialize.module.mall.vo.SmProductSimpleVo;
import com.xm.comment_serialize.module.user.bo.OrderCustomParameters;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("wphGoodsService")
public class GoodsServiceImpl extends GoodsServiceAbs {

    @Autowired
    private WphSdkComponent wphSdkComponent;
    @Autowired
    private ProfitService profitService;
    @Autowired
    private ProductTestService productTestService;
    @Value("${spring.profiles.active}")
    private String active;

    @Override
    public SmProductEntityEx detail(GoodsDetailForm goodsDetailForm) throws Exception {
        SmProductEntity smProductEntity = wphSdkComponent.detail(goodsDetailForm.getGoodsId());
        return profitService.calcProfit(
                smProductEntity,
                goodsDetailForm.getUserId(),
                goodsDetailForm.getShareUserId() != null,
                goodsDetailForm.getShareUserId());
    }

    @Override
    public List<SmProductEntity> details(GoodsDetailsForm goodsDetailsForm) throws Exception {
        return wphSdkComponent.details(goodsDetailsForm.getUserId(),goodsDetailsForm.getGoodsIds());
    }

    @Override
    public ShareLinkBo saleInfo(SaleInfoForm saleInfoForm) throws Exception {
        return wphSdkComponent.getShareLink(saleInfoForm.getPid(),saleInfoForm.getGoodsId());
    }
}
