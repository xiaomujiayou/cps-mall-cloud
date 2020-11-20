package com.xm.api_mall.controller;

import com.alibaba.fastjson.JSON;
import com.xm.api_mall.component.PddSdkComponent;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.annotation.Pid;
import com.xm.comment_serialize.module.mall.bo.PddAuthBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private PddSdkComponent pddSdkComponent;

    @GetMapping("/mgj")
    public void getMgjToken(HttpServletRequest request){
        System.out.println(JSON.toJSONString(request.getParameterMap()));
    }

    @GetMapping("/pdd/auth")
    public PddAuthBo getPddAuth(@LoginUser Integer userId, @Pid String pid) throws Exception{
        return pddSdkComponent.getAuthInfo(pid,userId);
    }
    @GetMapping("/pdd/isAuth")
    public Boolean isAuth(@LoginUser Integer userId, @Pid String pid) throws Exception{
        return pddSdkComponent.isRecord(pid,userId);
    }
}
