package com.xm.api_mall.controller;

import com.xm.api_mall.service.api.OptionService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.annotation.PlatformType;
import com.xm.comment_serialize.module.mall.entity.SmOptEntity;
import com.xm.comment_serialize.module.mall.form.OptionForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/option")
public class OptionController {

    @Autowired
    private OptionService optionService;

    @GetMapping
    public List<SmOptEntity> get(@LoginUser(necessary = false) @PlatformType OptionForm optionForm) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return optionService.childList(optionForm);
    }
}
