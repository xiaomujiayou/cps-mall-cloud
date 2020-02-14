package com.xm.api_mall.controller;

import com.xm.api_mall.service.PidService;
import com.xm.comment_serialize.module.mall.entity.SmPidEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 推广位相关接口
 */
@RestController
@RequestMapping("/pid")
public class PidController {

    @Autowired
    private PidService pidService;

    @GetMapping("/generate")
    public SmPidEntity generatePid() throws Exception {
        return pidService.generatePid();
    }
    @GetMapping
    public SmPidEntity getPid(Integer id) throws Exception {
        return pidService.getPid(id);
    }
}
