package com.xm.comment_mq.config;

import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;

public class BillMqConfig {
    public static final String EXCHANGE = "bill";
    public static final String KEY = "sucess";
    public static final String QUEUE = EXCHANGE + "." + KEY + ".queue";
    public static final Class MESSAGE_CLASS = SuBillEntity.class;
}
