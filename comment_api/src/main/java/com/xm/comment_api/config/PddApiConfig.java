package com.xm.comment_api.config;

import com.pdd.pop.sdk.http.PopHttpClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "pdd")
public class PddApiConfig {
    private String clientId;
    private String clientSecret;
    @Bean
    public PopHttpClient getPopHttpClient(){
        return new PopHttpClient(clientId,clientSecret);
    }
}

