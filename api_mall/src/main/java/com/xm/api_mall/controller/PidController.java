package com.xm.api_mall.controller;

import com.xm.api_mall.component.PlatformContext;
import com.xm.api_mall.service.PidService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.module.user.feign.UserFeignClient;
import com.xm.comment.response.Msg;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.mall.entity.SmPidEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public Msg<SmPidEntity> generatePid() throws Exception {
        return R.sucess(pidService.generatePid());
    }
    @GetMapping
    public Msg<SmPidEntity> getPid(Integer id) throws Exception {
        return R.sucess(pidService.getPid(id));
    }
}
