package com.xm.comment_api.config;

import com.xm.comment_api.client.MyTaobaoClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Data
@Configuration
@ConfigurationProperties(prefix = "tb")
public class TbApiConfig {

    private String url;
    private String appKey;
    private String appSecret;
    private Long adzoneId;

    @Bean
    public MyTaobaoClient getTbHttpClient(){
        MyTaobaoClient myTaobaoClient = new MyTaobaoClient(url,appKey,appSecret);
        return myTaobaoClient;
    }
}

