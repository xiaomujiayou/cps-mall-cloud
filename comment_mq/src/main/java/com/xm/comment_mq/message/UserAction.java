package com.xm.comment_mq.message;

import lombok.Data;

/**
 * 用户事件消息载体
 */
@Data
public class UserAction {
    //所属用户
    private Integer userId;
    //事件类型
    private UserActionEnum actionType;
    //数据载体
    private Object body;
}
