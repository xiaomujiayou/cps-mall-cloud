package com.xm.api_activite.service;

import com.xm.comment_serialize.module.active.entity.SaActiveEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.pay.entity.SpWxEntPayOrderInEntity;

import java.util.List;

public interface ActiveService {

    /**
     * 获取活动列表
     * @return
     */
    public List<SaActiveEntity> getList(Integer userId);

    /**
     * 获取活动详情
     * @return
     */
    public SaActiveEntity getDetail(Integer userId);

    /**
     * 获取活动收入
     * @param userId
     * @return
     */
    public Integer getProfit(Integer userId,Integer activeId);

    /**
     * 获取商品优惠详情
     * @param userId
     * @param productEntityExes
     * @return
     */
    List<SmProductEntityEx> goodsInfo(Integer userId, List<SmProductEntityEx> productEntityExes);

    /**
     * 获取单个商品优惠详情
     * @param userId
     * @param smProductEntityEx
     * @return
     */
    SmProductEntityEx goodsInfo(Integer userId, SmProductEntityEx smProductEntityEx);

    /**
     * 体现结果回调
     * @param spWxEntPayOrderInEntity
     */
    void onCashoutEntPayResult(SpWxEntPayOrderInEntity spWxEntPayOrderInEntity);

    /**
     * 自动返现回调
     * @param spWxEntPayOrderInEntity
     */
    void onCashoutAutoEntPayResult(SpWxEntPayOrderInEntity spWxEntPayOrderInEntity);
}
