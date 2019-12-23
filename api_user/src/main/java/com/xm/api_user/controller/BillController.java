package com.xm.api_user.controller;

import com.github.pagehelper.PageHelper;
import com.xm.api_user.mapper.SuBillMapper;
import com.xm.api_user.service.BillService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.response.Msg;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.List;

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
