package com.xm.wind_control;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.lang.reflect.Method;
import java.util.concurrent.locks.Lock;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WindControlApplicationTests {

    @Autowired
    private RedisLockRegistry redisLockRegistry;

    @Test
    public void contextLoads() {
        new Thread(()->{
            Lock lock1 = redisLockRegistry.obtain("1");
            System.out.println("111");
            lock1.lock();
            System.out.println("222");
        }).start();
    }

    @Test
    public void test(){
        new Thread(()->{
            Lock lock2 = redisLockRegistry.obtain("2");
            System.out.println("333");
            lock2.lock();
            System.out.println("444");
        }).start();
    }

}
