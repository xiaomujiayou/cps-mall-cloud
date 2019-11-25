package com.xm.api_user.message;

import com.alibaba.fastjson.JSON;
import com.xm.comment_mq.config.OrderMqConfig;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderReceiver {

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(OrderMqConfig.EXCHANGE),
            key = OrderMqConfig.KEY,
            value = @Queue(OrderMqConfig.QUEUE)
    ))
    public void onMessage(SuOrderEntity suOrderEntity){
        log.debug(JSON.toJSONString(suOrderEntity));
    }
}
