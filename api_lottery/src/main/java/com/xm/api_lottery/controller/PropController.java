package com.xm.api_lottery.controller;

import com.xm.api_lottery.service.PropService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment_serialize.module.lottery.vo.SlPropVo;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/prop")
public class PropController {

    @Autowired
    private PropService propService;

    /**
     * 道具列表
     * @param userId
     * @return
     */
    @GetMapping
    public List<SlPropVo> list(@LoginUser(necessary = false) Integer userId){
        return propService.getPropVo(userId);
    }
}

