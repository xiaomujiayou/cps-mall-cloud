package com.xm.api_user.config;

import com.xm.api_user.message.handler.MenuTipsHandler;
import com.xm.comment_mq.handler.MessageManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqMsgManagerConfig {

    @Bean
    public MessageManager messageManager(MenuTipsHandler menuTipsHandler){
        MessageManager messageManager = new MessageManager();
        messageManager.add(menuTipsHandler);
        return messageManager;
    }
}
