package com.xm.api_user.message;

import com.rabbitmq.client.Channel;
import com.xm.api_user.service.OrderService;
import com.xm.api_user.config.OrderDeadLetterConfig;
import com.xm.comment_mq.constant.RabbitMqConstant;
import com.xm.comment_mq.message.config.OrderMqConfig;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 系统收到订单
 */
@Slf4j
@Component
public class OrderReceiver {

    @Autowired
    private OrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(OrderMqConfig.EXCHANGE),
            key = OrderMqConfig.KEY,
            value = @Queue(value = OrderMqConfig.QUEUE,
                    arguments = {
                        @Argument(name = RabbitMqConstant.DEAD_QUEUE_ARG_EXCHANGE_NAME,value = OrderMqConfig.EXCHANGE),
                        @Argument(name = RabbitMqConstant.DEAD_QUEUE_ARG_KEY_NAME,value = OrderMqConfig.KEY_PROCESS_FAIL)
            })
    ))
    public void onMessage(SuOrderEntity suOrderEntity, Channel channel, Message message) throws IOException {
        Long msgId = message.getMessageProperties().getDeliveryTag();
        try{
            orderService.receiveOrderMsg(suOrderEntity);
        }catch (Exception e){
            channel.basicReject(msgId,false);
            log.error("消息：{} 平台：{} 单号：{} 处理失败 error：{}",msgId,suOrderEntity.getPlatformType(),suOrderEntity.getOrderSn(),e);
        } finally {
            channel.basicAck(msgId,false);
        }
    }
}
