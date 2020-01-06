package com.xm.api_mall.controller;

import com.xm.comment.response.Msg;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.mall.form.GetAppShareForm;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/app")
public class AppController {

    @GetMapping("/share")
    public Msg<Object> share(@Valid GetAppShareForm getAppShareForm , BindingResult bindingResult){
        if(getAppShareForm.getType().equals(1)){

        }
        return R.sucess(222);
    }
}
