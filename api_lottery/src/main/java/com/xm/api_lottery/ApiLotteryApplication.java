package com.xm.api_lottery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@ComponentScan("com.xm")
@EnableFeignClients(basePackages = {"com.xm.comment_feign.module.*.feign"})
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.xm.*.mapper","com.xm.*.mapper.custom"})
@SpringBootApplication
public class ApiLotteryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiLotteryApplication.class, args);
    }

}
