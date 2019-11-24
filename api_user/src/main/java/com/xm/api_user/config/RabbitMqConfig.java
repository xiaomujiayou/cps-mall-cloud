package com.xm.api_user.config;

import com.rabbitmq.http.client.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * 配置jackson序列化
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

    @Bean
    public Client getClient(RabbitProperties rabbitProperties){
        Client client = null;
        try {
            client = new Client("http://"+rabbitProperties.getHost()+":15672/api/", rabbitProperties.getUsername(), rabbitProperties.getPassword());
        } catch (MalformedURLException e) {
            log.error("{}",e);
        } catch (URISyntaxException e) {
            log.error("{}",e);
        }
        return client;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //每次请求发送消息数量
        factory.setPrefetchCount(1);
        //收到异常丢弃消息
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

}
