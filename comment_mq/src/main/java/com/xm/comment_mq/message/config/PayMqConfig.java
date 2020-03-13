package com.xm.comment_mq.message.config;

import org.springframework.amqp.core.ExchangeTypes;

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
    //微信企业付款
    public static final String KEY_WX_ENT_PAY = "wx.entpay";
    public static final String QUEUE_WX_ENT_PAY = KEY_WX_ENT_PAY + ".queue";


    //微信企业付款成功
    public static final String EXCHANGE_ENT = "pay.ent";
    public static final String EXCHANGE_ENT_TYPE = ExchangeTypes.FANOUT;
    //用户服务
    public static final String QUEUE_ENT_PAY_USER_SUCESS = EXCHANGE_ENT + ".sucess.user.queue";
    public static final String QUEUE_ENT_PAY_CRON_SUCESS = EXCHANGE_ENT + ".sucess.cron.queue";
}
