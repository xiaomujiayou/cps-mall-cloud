package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import lombok.Data;

@Data
public class UserSearchGoodsMessage extends AbsUserActionMessage {
    //搜索关键字
    private String Keywords;
}
