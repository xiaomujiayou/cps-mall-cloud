package com.xm.api_user.message;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.xm.api_user.service.OrderService;
import com.xm.comment_mq.config.OrderMqConfig;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OrderReceiver {

    @Autowired
    private OrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(OrderMqConfig.EXCHANGE),
            key = OrderMqConfig.KEY,
            value = @Queue(OrderMqConfig.QUEUE)
    ))
    public void onMessage(SuOrderEntity suOrderEntity, Channel channel, Message message) throws IOException {
        log.debug(JSON.toJSONString(suOrderEntity));
        orderService.receiveOrderMsg(suOrderEntity);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
