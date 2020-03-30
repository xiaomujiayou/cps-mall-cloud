package com.xm.api_mall.service.api.impl.mgj;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.xm.api_mall.component.MgjSdkComponent;
import com.xm.api_mall.component.PddSdkComponent;
import com.xm.api_mall.service.ProductTestService;
import com.xm.api_mall.service.ProfitService;
import com.xm.api_mall.service.api.GoodsService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("mgjGoodsService")
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private MgjSdkComponent mgjSdkComponent;
    @Autowired
    private ProfitService profitService;

    @Override
    public SmProductEntityEx detail(GoodsDetailForm goodsDetailForm) throws Exception {
        SmProductEntity smProductEntity = mgjSdkComponent.detail(
                goodsDetailForm.getGoodsId(),
                goodsDetailForm.getPid());
        return profitService.calcProfit(
                smProductEntity,goodsDetailForm.getUserId(),
                goodsDetailForm.getShareUserId() != null,
                goodsDetailForm.getShareUserId());
    }

    @Override
    public List<SmProductEntity> details(GoodsDetailsForm goodsDetailsForm) throws Exception {
        return mgjSdkComponent.details(goodsDetailsForm.getGoodsIds());
    }

    @Override
    public SmProductSimpleVo basicDetail(BaseGoodsDetailForm baseGoodsDetailForm) throws Exception {
        return null;
    }

    @Override
    public ShareLinkBo saleInfo(SaleInfoForm saleInfoForm) throws Exception {
        OrderCustomParameters parameters = new OrderCustomParameters();
        parameters.setUserId(saleInfoForm.getUserId());
        parameters.setShareUserId(saleInfoForm.getShareUserId());
        parameters.setFromApp(saleInfoForm.getAppType());
        return mgjSdkComponent.getShareLink(
                JSON.toJSONString(parameters),
                saleInfoForm.getPid(),
                saleInfoForm.getGoodsId(),
                saleInfoForm.getCouponId());
    }
}
