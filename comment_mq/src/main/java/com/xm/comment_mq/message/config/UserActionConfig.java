package com.xm.comment_mq.message.config;

import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import org.springframework.amqp.core.ExchangeTypes;

/**
 * 用户动态队列配置
 */
public class UserActionConfig {
    public static final String EXCHANGE = "user.action";
    public static final String EXCHANGE_TYPE = ExchangeTypes.FANOUT;
    //抽奖f服务队列
    public static final String QUEUE_LOTTERY = EXCHANGE + ".lottery.queue";
    //用户服务队列
    public static final String QUEUE_USER = EXCHANGE + ".user.queue";

}
