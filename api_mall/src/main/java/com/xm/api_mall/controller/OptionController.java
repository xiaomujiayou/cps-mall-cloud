package com.xm.api_mall.controller;

import com.xm.api_mall.service.OptionService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.R;
import com.xm.comment_serialize.module.mall.entity.SmOptEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/option")
public class OptionController {

    @Autowired
    private OptionService optionService;

    @GetMapping
    public List<SmOptEntity> get(@LoginUser(necessary = false) Integer userId,Integer parentOptId){
        return optionService.getChildOption(userId,parentOptId);
    }
}
