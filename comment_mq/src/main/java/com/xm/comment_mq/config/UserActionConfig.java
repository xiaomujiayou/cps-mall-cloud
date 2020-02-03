package com.xm.comment_mq.config;

import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import org.springframework.amqp.core.ExchangeTypes;

public class UserActionConfig {
    public static final String EXCHANGE = "user.action";
    public static final String EXCHANGE_TYPE = ExchangeTypes.FANOUT;
//    public static final String KEY = "original";
//    public static final String QUEUE = EXCHANGE  + "." + KEY + ".queue";
    public static final Class MESSAGE_CLASS = SuOrderEntity.class;
}
