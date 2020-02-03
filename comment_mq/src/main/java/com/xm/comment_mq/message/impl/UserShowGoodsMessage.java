package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import lombok.Data;

@Data
public class UserShowGoodsMessage extends AbsUserActionMessage {
    //用户浏览一个商品
    private SmProductEntityEx smProductEntity;
}
