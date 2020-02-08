package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import lombok.Data;


@Data
public class UserAddProxyMessage extends AbsUserActionMessage {

    public UserAddProxyMessage(Integer userId, Integer level, SuUserEntity proxyUser) {
        super(userId);
        this.level = level;
        this.proxyUser = proxyUser;
    }
    private UserActionEnum userActionEnum = UserActionEnum.USER_ADD_PROXY;
    //代理级别
    private Integer level;
    //代理用户
    private SuUserEntity proxyUser;
}
