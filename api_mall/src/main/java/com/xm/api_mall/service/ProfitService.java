package com.xm.api_mall.service;

import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;

/**
 * 佣金相关服务
 */
public interface ProfitService {

    /**
     * 计算商品佣金
     * @param smProductEntity
     * @param userId
     * @param isShare           :是否为分享商品
     * @return
     */
    public SmProductEntityEx calcProfit(SmProductEntity smProductEntity, Integer userId,Boolean isShare,Integer shareUserId);
}
