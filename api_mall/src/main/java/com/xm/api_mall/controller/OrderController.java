package com.xm.api_mall.controller;

import com.xm.api_mall.service.ProductApiService;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource(name = "pddProductApiService")
    private ProductApiService pddProductApiService;

    @GetMapping("/time")
    public Date getTime() throws Exception {
        return pddProductApiService.getTime();
    }
}
