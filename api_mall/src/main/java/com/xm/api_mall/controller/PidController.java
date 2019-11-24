package com.xm.api_mall.controller;

import com.xm.api_mall.component.PlatformContext;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.module.user.feign.UserFeignClient;
import com.xm.comment.response.Msg;
import com.xm.comment.response.R;
import org.springframework.beans.factory.annotation.Autowired;
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
    private PlatformContext productContext;
    @Autowired
    private UserFeignClient userFeignClient;

    @PostMapping
    public Msg<String> generatePid(@LoginUser Integer userId, Integer platformType) throws Exception {
        return R.sucess(productContext.platformType(platformType).getService().generatePid(userId));
    }
}
