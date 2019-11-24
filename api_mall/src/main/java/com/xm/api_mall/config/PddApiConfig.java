package com.xm.api_mall.config;

import com.pdd.pop.sdk.http.PopHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PddApiConfig {

    @Value("${pdd.client-id}")
    private String clientId;
    @Value("${pdd.client-secret}")
    private String clientSecret;

    @Bean
    public PopHttpClient getPopHttpClient(){
        return new PopHttpClient(clientId,clientSecret);
    }
}
