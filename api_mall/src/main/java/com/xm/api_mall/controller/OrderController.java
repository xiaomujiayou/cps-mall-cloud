package com.xm.api_mall.controller;

import com.xm.api_mall.service.ProductApiService;
import com.xm.comment.response.Msg;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.mall.form.OrderIncrementListForm;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource(name = "pddProductApiService")
    private ProductApiService pddProductApiService;

    /**
     * 拼多多按更新时间查询订单
     * @param form
     * @param bindingResult
     * @return
     * @throws Exception
     */
//    @GetMapping("/pdd/increment")
//    public Msg<PageBean<SuOrderEntity>> getIncrement(@Valid OrderIncrementListForm form, BindingResult bindingResult) throws Exception {
//        return R.sucess(pddProductApiService.getOrderByIncrement(form.getStartUpdateTime(),form.getEndUpdateTime(),form.getPageNum(),form.getPageSize()));
//    }

    @GetMapping("/time")
    public Msg<Date> getTime() throws Exception {
        return R.sucess(pddProductApiService.getTime());
    }



}
