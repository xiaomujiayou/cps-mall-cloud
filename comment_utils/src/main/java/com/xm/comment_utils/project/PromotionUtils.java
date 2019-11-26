package com.xm.comment_utils.project;

/**
 * 佣金工具
 */
public class PromotionUtils {

    /**
     * 按佣金比例计算
     * @param amount
     * @param rate
     * @return
     */
    public static int calcByRate(int amount,int rate){
        return amount * rate / 1000;
    }
}
