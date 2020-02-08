package com.xm.api_lottery.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rabbitmq.client.Channel;
import com.xm.comment_mq.config.OrderMqConfig;
import com.xm.comment_mq.config.UserActionConfig;
import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Console;
import java.io.IOException;

@Slf4j
@Component
public class UserActionReceiver {
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = UserActionConfig.EXCHANGE, type = ExchangeTypes.FANOUT),
            key = UserActionConfig.BINDING_KEY_LOTTERY,
            value = @Queue(UserActionConfig.QUEUE_LOTTERY)
    ))
    public void onMessage(Object userActionMessage, Channel channel, Message message) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        String jsonStr = new String(message.getBody());
        System.out.println(jsonStr);
        UserActionEnum userActionEnum = JSON.parseObject(jsonStr).getObject("userActionEnum", UserActionEnum.class);
        System.out.println(JSON.toJSONString(JSON.parseObject(jsonStr,userActionEnum.getMessageType()) ,SerializerFeature.PrettyFormat));
    }
}
