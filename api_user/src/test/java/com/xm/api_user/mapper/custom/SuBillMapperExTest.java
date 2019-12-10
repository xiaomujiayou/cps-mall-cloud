package com.xm.api_user.mapper.custom;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xm.api_user.ApiUserApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class SuBillMapperExTest extends ApiUserApplicationTests {

    @Autowired
    private SuBillMapperEx suBillMapperEx;

    @Test
    public void getOrderBill(){
        System.out.println(JSON.toJSONString(suBillMapperEx.getOrderBill(1,2,1,1), SerializerFeature.PrettyFormat));
    }
}