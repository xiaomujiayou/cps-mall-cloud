package com.xm.comment_api.config;

import com.vip.osp.sdk.context.ClientInvocationContext;
import com.vip.osp.sdk.context.InvocationContext;
import com.xm.comment_api.client.MyMogujieClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;

@Data
@Configuration
@ConfigurationProperties(prefix = "wph")
public class WphApiConfig {
    private String appKey;
    private String appSecret;
    private String url;
    private String weAppId;

    /**
     * 初始化唯品会api参数
     */
    @Bean
    public ClientInvocationContext clientInvocationContext(){
        ClientInvocationContext instance = new ClientInvocationContext();
        instance.setAppKey(appKey);
        instance.setAppSecret(appSecret);
        instance.setAppURL(url);
        return instance;
    }
}

