package com.xm.comment_mq.config;

import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import org.springframework.amqp.core.ExchangeTypes;

/**
 * 用户动态队列配置
 */
public class UserActionConfig {
    public static final String EXCHANGE = "user.action";
    public static final String EXCHANGE_TYPE = ExchangeTypes.FANOUT;
    public static final String ROUTTING_KEY = EXCHANGE + ".#";
    //抽奖f服务相关
    public static final String BINDING_KEY_LOTTERY = EXCHANGE + ".lottery";
    public static final String QUEUE_LOTTERY = BINDING_KEY_LOTTERY + ".queue";

    public static final Class MESSAGE_CLASS = SuOrderEntity.class;
}
