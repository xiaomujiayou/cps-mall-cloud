package com.xm.api_mall.service.impl;

import com.alibaba.fastjson.JSON;
import com.xm.api_mall.service.ProductTestService;
import com.xm.api_mall.service.api.GoodsService;
import com.xm.comment_mq.message.config.OrderMqConfig;
import com.xm.comment_serialize.module.mall.bo.ShareLinkBo;
import com.xm.comment_serialize.module.mall.constant.PlatformTypeConstant;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.GetProductSaleInfoForm;
import com.xm.comment_serialize.module.mall.form.GoodsDetailForm;
import com.xm.comment_serialize.module.user.constant.OrderStateConstant;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_utils.product.GenNumUtil;
import com.xm.comment_utils.project.PromotionUtils;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service("productTestService")
public class ProductTestServiceImpl implements ProductTestService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private GoodsService goodsService;

    @Override
    public ShareLinkBo saleInfo(Integer userId, String pid, GetProductSaleInfoForm productSaleInfoForm) throws Exception {
        ShareLinkBo shareLinkBo = createShareLinkBo();
        GoodsDetailForm goodsDetailForm = new GoodsDetailForm();
        goodsDetailForm.setUserId(userId);
        goodsDetailForm.setPid(pid);
        goodsDetailForm.setPlatformType(productSaleInfoForm.getPlatformType());
        goodsDetailForm.setGoodsId(productSaleInfoForm.getGoodsId());
        goodsDetailForm.setShareUserId(productSaleInfoForm.getShareUserId());
        goodsDetailForm.setAppType(productSaleInfoForm.getAppType());
        SmProductEntityEx smProductEntityEx = goodsService.detail(goodsDetailForm);
        SuOrderEntity suOrderEntity = createSuOrderEntity(smProductEntityEx,pid,userId,productSaleInfoForm);
        rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,suOrderEntity);
        Thread.sleep(1000);
        suOrderEntity.setState(OrderStateConstant.CONFIRM_RECEIPT);
        suOrderEntity.setOrderModifyAt(new Date());
        rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,suOrderEntity);
        Thread.sleep(1000);
        suOrderEntity.setOrderModifyAt(new Date());
//        suOrderEntity.setState(OrderStateConstant.CHECK_SUCESS);
        suOrderEntity.setState(OrderStateConstant.CHECK_FAIL);
        suOrderEntity.setFailReason("用户取消订单");
        rabbitTemplate.convertAndSend(OrderMqConfig.EXCHANGE,OrderMqConfig.KEY,suOrderEntity);
        return shareLinkBo;
    }

    private SuOrderEntity createSuOrderEntity(SmProductEntityEx item,String pid,Integer userId,GetProductSaleInfoForm productSaleInfoForm){
        SuOrderEntity orderEntity = new SuOrderEntity();
        orderEntity.setOrderSn(GenNumUtil.genOrderNum());
        orderEntity.setProductId(item.getGoodsId().toString());
        orderEntity.setProductName(item.getName());
        orderEntity.setImgUrl(item.getGoodsThumbnailUrl());
        orderEntity.setPlatformType(PlatformTypeConstant.PDD);
        orderEntity.setState(OrderStateConstant.PAY);
        orderEntity.setPId(pid);
        orderEntity.setOriginalPrice(item.getOriginalPrice());
        orderEntity.setQuantity(1);
        orderEntity.setAmount(item.getOriginalPrice() - item.getCouponPrice());
        orderEntity.setPromotionRate(item.getPromotionRate().intValue());
        orderEntity.setPromotionAmount(PromotionUtils.calcByRate(orderEntity.getAmount(),orderEntity.getPromotionRate()));
        orderEntity.setType(0);
        Map<String,Object> customParams = new HashMap<>();
        customParams.put("userId",userId);
        customParams.put("fromApp",productSaleInfoForm.getAppType());
        customParams.put("shareUserId",productSaleInfoForm.getShareUserId());
        orderEntity.setCustomParameters(JSON.toJSONString(customParams));
        orderEntity.setOrderModifyAt(new Date());
        return orderEntity;
    }

    private ShareLinkBo createShareLinkBo(){
        ShareLinkBo shareLinkBo = new ShareLinkBo();
        shareLinkBo.setWePagePath("test");
        return shareLinkBo;
    }
}
