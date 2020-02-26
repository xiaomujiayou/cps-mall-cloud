package com.xm.api_mall.config;

import com.codingapi.txlcn.tc.aspect.DataSourceAspect;
import com.xm.comment_mq.handler.MessageManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqMsgManagerConfig {

    @Bean
    public MessageManager messageManager(){
        MessageManager messageManager = new MessageManager();
        return messageManager;
    }
}