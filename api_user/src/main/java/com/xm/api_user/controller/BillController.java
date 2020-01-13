package com.xm.api_user.controller;

import com.xm.api_user.service.BillService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping
    public Msg get(@LoginUser Integer userId,Integer state,Integer type,Integer pageNum,Integer pageSize){
        return R.sucess(billService.getList(userId,state,type,pageNum,pageSize));
    }
}
