package com.xm.comment_serialize.module.user.vo;

import lombok.Data;

@Data
public class UserProfitVo {
    //领取优惠券总额
    private Integer totalCoupon;
    //历史返现总额(已返)
    private Integer totalCommission;
    //当天返现总额(新成交)
    private Integer todayProfit;
    //总消费
    private Integer totalConsumption;
    //分享订单总额(已下单付款)
    private Integer totalShare;
    //锁定用户
    private Integer totalProxyUser;
}
