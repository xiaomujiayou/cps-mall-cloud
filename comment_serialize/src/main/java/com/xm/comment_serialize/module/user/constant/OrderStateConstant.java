package com.xm.comment_serialize.module.user.constant;

public class OrderStateConstant {
    //未支付
    public static final int UN_PAY = -1;
    //已支付
    public static final int PAY = 0;
    //已发货
    public static final int SHIPPED = 1;
    //确认收货
    public static final int CONFIRM_RECEIPT = 2;
    //审核成功
    public static final int CHECK_SUCESS = 3;
    //审核失败
    public static final int CHECK_FAIL = 4;
    //已结算
    public static final int ALREADY_SETTLED = 5;
    //一般商品
    public static final int NORMAL = 8;
    //已处罚
    public static final int PUNISH = 10;
}
