package com.xm.api_mall;

import com.alibaba.fastjson.JSON;
import com.xm.comment_feign.module.user.feign.UserFeignClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiMallApplicationTests {

    @Autowired
    private UserFeignClient userFeignClient;

    @Test
    public void contextLoads() {
    }

}
