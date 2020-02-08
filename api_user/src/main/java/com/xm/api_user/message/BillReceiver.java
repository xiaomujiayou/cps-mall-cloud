package com.xm.api_user.message;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.xm.comment_mq.config.BillMqConfig;
import com.xm.comment_mq.config.OrderMqConfig;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class BillReceiver {

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(BillMqConfig.EXCHANGE),
            key = BillMqConfig.KEY,
            value = @Queue(BillMqConfig.QUEUE)
    ))
    public void onMessage(SuBillEntity suBillEntity, Channel channel, Message message) throws IOException {
        log.debug("发放佣金");
        log.debug(JSON.toJSONString(suBillEntity));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
