package com.xm.api_mall.controller;

import com.xm.api_mall.service.ProfitService;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.R;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.CalcProfitForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/profit")
@RestController
public class ProfitController {

    @Autowired
    private ProfitService profitService;

    @PostMapping("/calc")
    public Msg<List<SmProductEntityEx>> calc(@RequestBody CalcProfitForm calcProfitForm){
        return R.sucess( profitService.calcProfit(calcProfitForm.getSmProductEntities(),calcProfitForm.getUserId()));
    }
}
