package com.xm.api_user.config;

import com.xm.comment_mq.message.config.OrderMqConfig;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单死信队列的配置
 * 用于订单处理失败
 */
@Configuration
public class OrderDeadLetterConfig {

    // 声明死信队列
    @Bean
    public Queue orderLetterQueue(){
        return new Queue(OrderMqConfig.QUEUE_PROCESS_FAIL,true);
    }

    // 声明死信队列绑定关系
    @Bean
    public Binding orderLetterBinding(Queue orderLetterQueue){
        return BindingBuilder.bind(orderLetterQueue).to(new DirectExchange(OrderMqConfig.EXCHANGE)).with(OrderMqConfig.KEY_PROCESS_FAIL);
    }
}