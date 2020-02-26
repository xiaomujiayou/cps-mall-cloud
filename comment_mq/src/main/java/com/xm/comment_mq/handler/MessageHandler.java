package com.xm.comment_mq.handler;

import com.xm.comment_mq.message.AbsUserActionMessage;

import java.util.List;

/**
 * 用户动作消息处理器
 */
public interface MessageHandler {

    /**
     * 要处理的消息类型
     */
    public List<Class> getType();

    /**
     * 处理消息
     * @param message
     */
    public void handle(AbsUserActionMessage message);

    /**
     * 处理异常
     * @param e
     */
    public void onError(Exception e);

}
