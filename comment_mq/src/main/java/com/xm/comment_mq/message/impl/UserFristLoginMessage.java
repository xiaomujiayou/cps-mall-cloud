package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import lombok.Data;


@Data
public class UserFristLoginMessage extends AbsUserActionMessage {

    public UserFristLoginMessage(Integer userId, SuUserEntity userEntity) {
        super(userId);
        this.userEntity = userEntity;
    }
    private UserActionEnum userActionEnum = UserActionEnum.USER_FRIST_LOGIN;
    //用户信息
    private SuUserEntity userEntity;
}
