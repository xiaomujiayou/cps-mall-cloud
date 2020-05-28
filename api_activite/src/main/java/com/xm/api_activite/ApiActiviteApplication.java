package com.xm.api_activite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@EnableCaching
@ComponentScan("com.xm")
@EnableFeignClients(basePackages = {"com.xm.comment_feign.module.*.feign"})
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.xm.*.mapper","com.xm.*.mapper.custom"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ApiActiviteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiActiviteApplication.class, args);
    }

}
