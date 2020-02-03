package com.xm.comment_mq.config;

import com.xm.comment_serialize.module.user.entity.SuOrderEntity;

public class OrderMqConfig {
    public static final String EXCHANGE = "order";
    public static final String KEY = "original";
    public static final String QUEUE = EXCHANGE  + "." + KEY + ".queue";
    public static final Class MESSAGE_CLASS = SuOrderEntity.class;
}
