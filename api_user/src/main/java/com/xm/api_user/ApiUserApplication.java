package com.xm.api_user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

//开启分布式事务
//@EnableDistributedTransaction
@ComponentScan("com.xm")
@EnableFeignClients(basePackages = {"com.xm.comment_feign.module.*.feign"})
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.xm.*.mapper","com.xm.*.mapper.custom"})
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ApiUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiUserApplication.class, args);
    }

}
