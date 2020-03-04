package com.xm.api_user.controller;

import com.xm.api_user.service.OrderService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.R;
import com.xm.comment_serialize.module.user.dto.OrderBillDto;
import com.xm.comment_serialize.module.user.form.GetOrderForm;
import com.xm.comment_serialize.module.user.vo.OrderBillVo;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 获取用户订单
     * @param userId
     * @param getOrderForm
     * @param bindingResult
     * @return
     */
    @GetMapping
    public PageBean<OrderBillVo> myOrder(@LoginUser Integer userId, @Valid GetOrderForm getOrderForm, BindingResult bindingResult){
        PageBean<OrderBillDto> pageBean = orderService.getOrderBill(userId,getOrderForm.getType(),getOrderForm.getPlatformType(),getOrderForm.getState(),getOrderForm.getPageNum(),getOrderForm.getPageSize());
        List<OrderBillVo> vos = pageBean.getList().stream().map(o->{
            OrderBillVo vo = new OrderBillVo();
            vo.setOrderNum(o.getSuOrderEntity().getOrderSn());
            vo.setUserName(o.getSuUserEntity().getNickname());
            vo.setHeadImg(o.getSuUserEntity().getHeadImg());
            vo.setProductImgUrl(o.getSuOrderEntity().getImgUrl());
            vo.setTitle(o.getSuOrderEntity().getProductName());
            vo.setPlatformType(o.getSuOrderEntity().getPlatformType());
            vo.setGoodsId(o.getSuOrderEntity().getProductId());
            vo.setBillId(o.getId());
            vo.setOrderState(o.getSuOrderEntity().getState());
            vo.setState(o.getState());
            vo.setOriginalPrice(o.getSuOrderEntity().getOriginalPrice());
            vo.setQuantity(o.getSuOrderEntity().getQuantity());
            vo.setAmount(o.getSuOrderEntity().getAmount());
            vo.setMoney(o.getMoney());
            vo.setCreateTime(o.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
        PageBean<OrderBillVo> result = new PageBean<>(vos);
        result.setPageNum(pageBean.getPageNum());
        result.setPageSize(pageBean.getPageSize());
        result.setTotal(pageBean.getTotal());
        return result;
    }
}
