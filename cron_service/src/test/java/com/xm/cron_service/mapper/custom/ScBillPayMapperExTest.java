package com.xm.cron_service.mapper.custom;

import com.alibaba.fastjson.JSON;
import com.xm.cron_service.CronServiceApplicationTests;
import org.aspectj.lang.annotation.Aspect;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

public class ScBillPayMapperExTest extends CronServiceApplicationTests {

    @Autowired
    private ScBillPayMapperEx scBillPayMapperEx;

    @Test
    public void genPayBillTest(){
        System.out.println(JSON.toJSONString(scBillPayMapperEx.genPayBill(30,0,2,new Date())));
    }
}