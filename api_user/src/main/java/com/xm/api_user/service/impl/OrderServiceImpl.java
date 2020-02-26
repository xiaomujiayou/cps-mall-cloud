package com.xm.api_user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.xm.api_user.mapper.SuOrderMapper;
import com.xm.api_user.mapper.custom.SuBillMapperEx;
import com.xm.api_user.service.BillService;
import com.xm.api_user.service.OrderService;
import com.xm.api_user.service.ShareService;
import com.xm.comment_serialize.module.user.constant.OrderStateConstant;
import com.xm.comment_serialize.module.user.dto.OrderBillDto;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.Date;
import java.util.List;

/**
 * 计算收益分配到各个收益用户中
 * ①:分享订单只有【购买者】和【分享者】受益
 * ②:其他订单【购买者】、【代理人】受益
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Lazy
    @Autowired
    private OrderService orderService;
    @Autowired
    private SuOrderMapper suOrderMapper;
    @Autowired
    private BillService billService;
    @Autowired
    private SuBillMapperEx suBillMapperEx;
    @Autowired
    private ShareService shareService;

    /**
     * 处理订单消息
     * ①:添加新订单
     * ②:更新旧订单
     * @param order
     */
    @LcnTransaction
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void receiveOrderMsg(SuOrderEntity order) {
        //判断是否为新收录订单（系统未曾收录的）
        SuOrderEntity oldOrder = getOldOrder(order);
        if(oldOrder == null){
            //收录订单并计算相关账单
            orderService.onOrderCreate(order);
        }else if(repeated(oldOrder,order))          //是否重复收录
            return;
        //更新订单状态，并根据情况发放佣金
        orderService.updateOrderState(order,oldOrder);
    }

    /**
     * 订单是否重复
     * @param oldOrder
     * @param order
     * @return
     */
    private boolean repeated(SuOrderEntity oldOrder, SuOrderEntity order) {
        if(oldOrder == null)
            return false;
        if(oldOrder.getPlatformType().equals(order.getPlatformType()) && oldOrder.getState().equals(order.getState()))
            return true;
        return false;
    }

    /**
     * 获取旧订单
     * @param order
     * @return      :null标识该订单未曾收录
     */
    private SuOrderEntity getOldOrder(SuOrderEntity order){
        PageHelper.startPage(1,1,false);
        SuOrderEntity example = new SuOrderEntity();
        example.setOrderSn(order.getOrderSn());
        example = suOrderMapper.selectOne(example);
        if(example == null)
            return null;
        return example;
    }

    /**
     * 收到一笔新订单(系统不存在的)
     * ①:入库保存
     * ②:计算相关用户收益
     * @param order
     */
    @LcnTransaction
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onOrderCreate(SuOrderEntity order) {
        //保存订单
        JSONObject params = JSON.parseObject(order.getCustomParameters());
        Integer userId = params.getInteger("userId");
        Integer shareUserId = params.getInteger("shareUserId");
        Integer fromApp = params.getInteger("fromApp");
        order.setUserId(userId);
        order.setShareUserId(shareUserId);
        if(order.getShareUserId() != null && !order.getShareUserId().equals("")){
            order.setFormType(2);
            //添加分享记录
            shareService.buy(order);
        }else {
            order.setFormType(1);
        }
        order.setFromApp(fromApp);
        order.setCreateTime(new Date());
        suOrderMapper.insertUseGeneratedKeys(order);
        //创建订单收益账单
        billService.createByOrder(order);
    }

    /**
     * 更新订单状态
     * 处理达到要求的订单
     * @param newOrder
     * @param oldOrder
     */
    @LcnTransaction
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOrderState(SuOrderEntity newOrder,SuOrderEntity oldOrder) {
        if(checkState(newOrder,oldOrder, OrderStateConstant.CHECK_SUCESS, OrderStateConstant.ALREADY_SETTLED)){
            //达到发放状态，发放佣金
            billService.payOutOrderBill(newOrder);
        }
        if(checkState(newOrder,oldOrder, OrderStateConstant.CHECK_FAIL, OrderStateConstant.PUNISH)){
            //达到失败状态，更新状态
            billService.invalidOrderBill(newOrder);
        }
    }

    @Override
    public PageBean<OrderBillDto> getOrderBill(Integer userId,Integer type, Integer platformType, Integer state, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        OrderByHelper.orderBy("sb.create_time desc");
        List<OrderBillDto> list = suBillMapperEx.getOrderBill(userId ,type,platformType,state);
        return new PageBean<>(list);
    }

    /**
     * 订单是否到达预期状态
     * @param newOrder
     * @param oldOrder
     * @param orderState    :预期状态(OrderStateConstant)
     * @return
     */
    private boolean checkState(SuOrderEntity newOrder,SuOrderEntity oldOrder,Integer... orderState){
         List<Integer> states = Lists.newArrayList(orderState);
         if(oldOrder == null){
             //老订单不存在，新订单达到要求即可发放收益
             if(states.contains(newOrder.getState()))
                 return true;
         }else {
             //老订单存在，则只在状态变更时发放收益
             if(states.contains(newOrder.getState()) && !states.contains(oldOrder.getState()))
                 return true;
         }
         return false;
    }

}
