package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import lombok.Data;

@Data
public class UserOpenAppMessage extends AbsUserActionMessage {
    //app类型
    private Integer appType;
}
