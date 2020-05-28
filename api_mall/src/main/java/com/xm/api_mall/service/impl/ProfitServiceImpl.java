package com.xm.api_mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xm.api_mall.service.ConfigService;
import com.xm.api_mall.service.ProfitService;
import com.xm.comment.utils.GoodsPriceUtil;
import com.xm.comment_feign.module.active.feign.ActiveFeignClient;
import com.xm.comment_serialize.module.mall.constant.ConfigEnmu;
import com.xm.comment_serialize.module.mall.constant.ConfigTypeConstant;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("profitService")
public class ProfitServiceImpl implements ProfitService {

    @Autowired
    private ConfigService configService;
    @Autowired
    private ActiveFeignClient activeFeignClient;

    @Override
    public SmProductEntityEx calcProfit(SmProductEntity smProductEntity, Integer userId,Boolean isShare,Integer shareUserId) {

        //去掉自己分享
        if(userId != null && userId.equals(shareUserId)){
            isShare = false;
            shareUserId = null;
        }
        Integer configType = ConfigTypeConstant.PROXY_CONFIG;
        Integer buyRate = Integer.valueOf(configService.getConfig(isShare?shareUserId:userId, isShare?ConfigEnmu.PRODUCT_SHARE_BUY_RATE:ConfigEnmu.PRODUCT_BUY_RATE,isShare?ConfigTypeConstant.SELF_CONFIG: configType).getVal());
        Integer shareRate = Integer.valueOf(configService.getConfig(userId, ConfigEnmu.PRODUCT_SHARE_USER_RATE, configType).getVal());
        SmProductEntityEx result = calcProfit(userId,smProductEntity,buyRate,shareRate);
        //添加活动信息
        result =activeFeignClient.goodsActiveInfo(userId,result);
        return result;
    }

    @Override
    public List<SmProductEntityEx> calcProfit(List<SmProductEntity> smProductEntitys, Integer userId) {
        Integer configType = ConfigTypeConstant.PROXY_CONFIG;
        Integer buyRate = Integer.valueOf(configService.getConfig(userId, ConfigEnmu.PRODUCT_BUY_RATE,configType).getVal());
        Integer shareRate = Integer.valueOf(configService.getConfig(userId, ConfigEnmu.PRODUCT_SHARE_USER_RATE, configType).getVal());
        List<SmProductEntityEx> list = smProductEntitys.stream().map(o->{
            return calcProfit(userId,o,buyRate,shareRate);
        }).collect(Collectors.toList());
        //添加活动信息
        list = activeFeignClient.goodsActiveInfo(userId,list);
        return list;
    }

    private SmProductEntityEx calcProfit(Integer userId,SmProductEntity smProductEntity,Integer buyRate,Integer shareRate){
        SmProductEntityEx smProductEntityEx = new SmProductEntityEx();
        BeanUtil.copyProperties(smProductEntity,smProductEntityEx);
        smProductEntityEx.setUserId(userId);
        smProductEntityEx.setBuyRate(buyRate);
        smProductEntityEx.setShareRate(shareRate);
        smProductEntityEx.setBuyPrice(GoodsPriceUtil.type(smProductEntity.getType()).calcUserBuyProfit(smProductEntity,buyRate.doubleValue()).intValue());
        smProductEntityEx.setSharePrice(GoodsPriceUtil.type(smProductEntity.getType()).calcUserShareProfit(smProductEntity,shareRate.doubleValue()).intValue());
        return smProductEntityEx;
    }
}
