package com.xm.comment_mq.message.config;

/**
 * 支付服务mq配置
 */
public class PayMqConfig {
    public static final String EXCHANGE = "pay";
    //微信支付回调队列
    public static final String KEY_WX_NOTIFY = "wx.notify";
    public static final String QUEUE_WX_NOTIFY = EXCHANGE  + "." + KEY_WX_NOTIFY + ".queue";
    //微信支付回调死信队列
    public static final String KEY_WX_NOTIFY_FAIL = "wx.notify.fail";
    public static final String QUEUE_WX_NOTIFY_FAIL= EXCHANGE  + "." + KEY_WX_NOTIFY_FAIL + ".queue";
}
