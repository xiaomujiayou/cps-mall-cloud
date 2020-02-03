package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import lombok.Data;

@Data
public class UserShareAppMessage extends AbsUserActionMessage {
    //点击用户id
    private Integer clickUserId;
    //app类型
    private Integer appType;
}
