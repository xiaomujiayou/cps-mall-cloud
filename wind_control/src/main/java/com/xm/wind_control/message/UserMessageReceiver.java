package com.xm.wind_control.message;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.xm.comment_mq.message.AbsMessageReceiver;
import com.xm.comment_mq.message.config.UserActionConfig;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Component
@Aspect
public class UserMessageReceiver extends AbsMessageReceiver{

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = UserActionConfig.EXCHANGE, type = UserActionConfig.EXCHANGE_TYPE),
            value = @Queue(UserActionConfig.QUEUE_WIND)
    ))
    @Override
    public void onMessage(JSONObject jsonMessage, Channel channel, Message message) throws IOException {
        super.onMessage(jsonMessage, channel, message);
    }

}
