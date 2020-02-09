package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import lombok.Data;

@Data
public class UserOpenAppMessage extends AbsUserActionMessage {
    public UserOpenAppMessage(Integer userId, Integer appType) {
        super(userId);
        this.appType = appType;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.USER_OPEN_APP;
    //app类型
    private Integer appType;
}
