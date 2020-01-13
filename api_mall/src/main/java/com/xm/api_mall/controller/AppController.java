package com.xm.api_mall.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/app")
public class AppController {

    @GetMapping("/mgj")
    public void getMgjToken(HttpServletRequest request){
        System.out.println(JSON.toJSONString(request.getParameterMap()));
    }
}
