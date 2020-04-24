package com.xm.wind_control.config;

import com.xm.comment_mq.constant.RabbitMqConstant;
import com.xm.comment_mq.message.config.BillMqConfig;
import com.xm.comment_mq.message.config.PayMqConfig;
import com.xm.comment_mq.message.config.WindMqConfig;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.xm.comment_mq.constant.RabbitMqConstant.DELAY_EXCHANGE_TARGET_TYPE;
import static com.xm.comment_mq.constant.RabbitMqConstant.DELAY_EXCHANGE_TYPE;

/**
 * 信用支付到期死信
 * 用于信用支付延期付款
 */
//@Configuration
public class CreditDelayMqConfig {

//    @Bean
//    public Queue creditDelayQueue(){
//        Map<String, Object> args = new HashMap<String, Object>();
//        //默认延时时间
//        args.put(RabbitMqConstant.TTL_QUEUE_ARG_NAME,1000 * 60 * 60 * 24 * 15); //15天
//        args.put(RabbitMqConstant.DEAD_QUEUE_ARG_EXCHANGE_NAME, WindMqConfig.EXCHANGE);
//        args.put(RabbitMqConstant.DEAD_QUEUE_ARG_KEY_NAME,WindMqConfig.KEY_CREDIT_DELAY_SUCESS);
//        args.put(RabbitMqConstant.TTL_QUEUE_EXPIRES_NAME,WindMqConfig.KEY_CREDIT_DELAY_SUCESS);
//        return new Queue(WindMqConfig.QUEUE_CREDIT_DELAY,true,false,false,args);
//    }
//    @Bean
//    public DirectExchange creditExchange(){
//        return new DirectExchange(WindMqConfig.EXCHANGE);
//    }
//
//    @Bean
//    public Binding billLetterBinding(Queue creditDelayQueue,DirectExchange creditExchange){
//        return BindingBuilder.bind(creditDelayQueue).to(creditExchange).with(WindMqConfig.KEY_CREDIT_DELAY);
//    }

//    @Bean
//    public Queue creditDelayQueue(){
//        return new Queue(WindMqConfig.QUEUE_CREDIT_DELAY,true,false,false);
//    }
//    @Bean
//    CustomExchange creditExchange() {
//        Map<String, Object> args = new HashMap<String, Object>();
//        args.put(DELAY_EXCHANGE_TARGET_TYPE, ExchangeTypes.DIRECT);
//        return new CustomExchange(WindMqConfig.EXCHANGE, DELAY_EXCHANGE_TYPE, true, false, args);
//    }
//    @Bean
//    Binding billLetterBinding(Queue creditDelayQueue, Exchange creditExchange) {
//        return BindingBuilder.bind(creditDelayQueue).to(creditExchange).with(WindMqConfig.KEY_CREDIT_DELAY).noargs();
//    }
}