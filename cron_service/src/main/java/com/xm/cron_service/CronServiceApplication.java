package com.xm.cron_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

//开启分布式事务
//@EnableDistributedTransaction
@EnableScheduling
@ComponentScan("com.xm")
@MapperScan(basePackages = {"com.xm.*.mapper","com.xm.*.mapper.custom"})
@EnableFeignClients(basePackages = {"com.xm.comment_feign.module.*.feign"})
@EnableDiscoveryClient
@SpringBootApplication
public class CronServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CronServiceApplication.class, args);
    }
}
