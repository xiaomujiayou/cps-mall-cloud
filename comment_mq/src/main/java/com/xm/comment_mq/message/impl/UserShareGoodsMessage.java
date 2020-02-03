package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import lombok.Data;

@Data
public class UserShareGoodsMessage extends AbsUserActionMessage {
    private Integer clickUserId;
    private SmProductEntity smProductEntity;
}
