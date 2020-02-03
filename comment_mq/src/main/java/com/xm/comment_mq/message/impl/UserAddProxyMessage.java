package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import lombok.Data;


@Data
public class UserAddProxyMessage extends AbsUserActionMessage {
    //代理级别
    private Integer level;
    //代理用户
    private SuUserEntity proxyUser;
}
