package com.xm.comment.utils;

import cn.hutool.core.util.NumberUtil;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;

public class GoodsPriceUtil {

    private Integer platfromType;

    private GoodsPriceUtil(){}

    public static GoodsPriceUtil type(Integer platformType){
        GoodsPriceUtil goodsPriceUtil = new GoodsPriceUtil();
        goodsPriceUtil.platfromType = platformType;
        return goodsPriceUtil;
    }

    /**
     * 根据费率计算商品总佣金
     *
     * (原始价 - 代金卷) * 佣金比例
     *
     * @param originalPrice     :原始价格
     * @param couponPrice       :代金卷价格
     * @param profitRate        :佣金比例
     * @return
     */
    public Double calcProfit(Double originalPrice,Double couponPrice ,Double profitRate){
        return NumberUtil.div(NumberUtil.mul((Double)NumberUtil.sub(originalPrice,couponPrice),profitRate),1000);
    }
    /**
     * 根据费率计算商品总佣金
     *
     * (原始价 - 代金卷) * 佣金比例
     *
     * @param smProductEntity     :要计算的商品
     * @return
     */
    public Double calcProfit(SmProductEntity smProductEntity){
        return NumberUtil.div(NumberUtil.mul(NumberUtil.sub(smProductEntity.getOriginalPrice(),smProductEntity.getCouponPrice()),smProductEntity.getPromotionRate()),1000).doubleValue();
    }

    /**
     * 计算用户分享佣金
     * @param originalPrice
     * @param couponPrice
     * @param profitRate
     * @param userShareRate         :分享费率
     * @return
     */
    public Double calcUserShareProfit(Double originalPrice,Double couponPrice ,Double profitRate,Double userShareRate){
        return NumberUtil.div(NumberUtil.mul(calcProfit(originalPrice,couponPrice,profitRate),userShareRate),1000);
    }
    /**
     * 计算用户分享佣金
     * @param profit                :商品总佣金
     * @param userShareRate         :分享费率
     * @return
     */
    public Double calcUserShareProfit(Double profit,Double userShareRate){
        return NumberUtil.div(NumberUtil.mul(profit,userShareRate),1000);
    }

    /**
     * 计算用户分享佣金
     * @param smProductEntity       :商品
     * @param userShareRate         :分享费率
     * @return
     */
    public Double calcUserShareProfit(SmProductEntity smProductEntity,Double userShareRate){
        return NumberUtil.div(NumberUtil.mul(calcProfit(smProductEntity),userShareRate),1000);
    }

    /**
     * 计算用户分享购买佣金
     * @param originalPrice
     * @param couponPrice
     * @param profitRate
     * @param userShareBuyRate         :分享费率
     * @return
     */
    public Double calcUserShareBuyProfit(Double originalPrice,Double couponPrice ,Double profitRate,Double userShareBuyRate){
        return NumberUtil.div(NumberUtil.mul(calcProfit(originalPrice,couponPrice,profitRate),userShareBuyRate),1000);
    }
    /**
     * 计算用户分享购买佣金
     * @param profit                :商品总佣金
     * @param userShareBuyRate         :分享费率
     * @return
     */
    public Double calcUserShareBuyProfit(Double profit,Double userShareBuyRate){
        return NumberUtil.div(NumberUtil.mul(profit,userShareBuyRate),1000);
    }

    /**
     * 计算用户分享购买佣金
     * @param smProductEntity          :商品总佣金
     * @param userShareBuyRate         :分享费率
     * @return
     */
    public Double calcUserShareBuyProfit(SmProductEntity smProductEntity,Double userShareBuyRate){
        return NumberUtil.div(NumberUtil.mul(calcProfit(smProductEntity),userShareBuyRate),1000);
    }

    /**
     * 计算用户直接购买佣金
     * @param profit                :商品总佣金
     * @param userBuyRate           :购买费率
     * @return
     */
    public Double calcUserBuyProfit(Double profit,Double userBuyRate){
        return NumberUtil.div(NumberUtil.mul(profit,userBuyRate),1000);
    }

    /**
     * 计算用户直接购买佣金
     * @param originalPrice
     * @param couponPrice
     * @param profitRate
     * @param userBuyRate         :购买费率
     * @return
     */
    public Double calcUserBuyProfit(Double originalPrice,Double couponPrice ,Double profitRate,Double userBuyRate){
        return NumberUtil.div(NumberUtil.mul(calcProfit(originalPrice,couponPrice,profitRate),userBuyRate),1000);
    }

    /**
     * 计算用户直接购买佣金
     * @param smProductEntity
     * @param userBuyRate         :购买费率
     * @return
     */
    public Double calcUserBuyProfit(SmProductEntity smProductEntity,Double userBuyRate){
        return NumberUtil.div(NumberUtil.mul(calcProfit(smProductEntity),userBuyRate),1000);
    }

}
