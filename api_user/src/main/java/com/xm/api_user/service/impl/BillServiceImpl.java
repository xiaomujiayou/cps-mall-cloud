package com.xm.api_user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.google.common.collect.Lists;
import com.xm.api_user.mapper.SuBillMapper;
import com.xm.api_user.mapper.SuUserMapper;
import com.xm.api_user.service.BillService;
import com.xm.comment.module.mall.feign.MallFeignClient;
import com.xm.comment_mq.config.BillMqConfig;
import com.xm.comment_serialize.module.mall.constant.ConfigEnmu;
import com.xm.comment_serialize.module.mall.constant.ConfigTypeConstant;
import com.xm.comment_serialize.module.user.constant.BillStateConstant;
import com.xm.comment_serialize.module.user.constant.BillTypeConstant;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_utils.project.PromotionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("billService")
public class BillServiceImpl implements BillService {

    @Autowired
    private SuBillMapper suBillMapper;
    @Autowired
    private MallFeignClient mallFeignClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SuUserMapper suUserMapper;

    @LcnTransaction
    @Transactional
    @Override
    public void createByOrder(SuOrderEntity order) {
        //订单相关账单存在则返回
        List<SuBillEntity> userRelatedBills = getOrderRelatedBill(order);
        if(userRelatedBills != null && userRelatedBills.size() > 0)
            return;
        JSONObject params = JSON.parseObject(order.getCustomParameters());
        Integer shareUserId = params.getInteger("fromUser");
        if(shareUserId == null){
            //生成正常下单账单
            createNormalOrderBill(order);
        }else {
            //生成分享订单账单
            createShareOrderBill(shareUserId,order);
        }
    }

    /**
     * 创建正常购买流程账单
     * @param order
     */
    private void createNormalOrderBill(SuOrderEntity order){
        //属于正常购买订单
        //生成购买者账单
        Integer buyUserRate = Integer.valueOf(mallFeignClient.getOneConfig(
                order.getUserId(),
                ConfigEnmu.PRODUCT_BUY_RATE.getName(),
                ConfigTypeConstant.SYS_CONFIG).getData().getVal());
        SuBillEntity buyUserBill = createOrderBill(order.getUserId(),order,buyUserRate);
        suBillMapper.insertSelective(buyUserBill);
        //生成代理账单
        //获取代理层级
        Integer proxyLevel = Integer.valueOf(mallFeignClient.getOneConfig(
                null,
                ConfigEnmu.PROXY_LEVEL.getName(),
                ConfigTypeConstant.SYS_CONFIG).getData().getVal());
        List<Integer> proxyRate = Lists.newArrayList(mallFeignClient.getOneConfig(
                null,
                ConfigEnmu.PRODUCT_PROXY_RATE.getName(),
                ConfigTypeConstant.SYS_CONFIG).getData().getVal().split(","))
                .stream()
                .map(o->{return Integer.valueOf(o);})
                .collect(Collectors.toList());

        List<SuUserEntity> proxyUsers = getParentUser(order.getUserId(),proxyLevel);
        for (int i = 0; i < proxyLevel; i++) {
            if(proxyUsers == null || proxyUsers.size() <= 0 || proxyUsers.get(i) == null)
                break;
            SuBillEntity proxyBill = createOrderBill(proxyUsers.get(i).getId(),order,proxyRate.get(i));
            suBillMapper.insertSelective(proxyBill);
        }
    }

    /**
     * 创建分享订单所属账单
     * @param shareUserId
     * @param order
     */
    private void createShareOrderBill(Integer shareUserId,SuOrderEntity order){
        //属于分享订单
        //生成分享者账单
        Integer shareUserRate = Integer.valueOf(mallFeignClient.getOneConfig(
                shareUserId,
                ConfigEnmu.PRODUCT_SHARE_USER_RATE.getName(),
                ConfigTypeConstant.SYS_CONFIG).getData().getVal());
        SuBillEntity shareUserBill = createOrderBill(shareUserId,order,shareUserRate);
        suBillMapper.insertSelective(shareUserBill);
        //生成购买者订单
        Integer buyUserRate = Integer.valueOf(mallFeignClient.getOneConfig(
                order.getUserId(),
                ConfigEnmu.PRODUCT_SHARE_BUY_RATE.getName(),
                ConfigTypeConstant.SYS_CONFIG).getData().getVal());
        SuBillEntity buyUserBill = createOrderBill(order.getUserId(),order,buyUserRate);
        suBillMapper.insertSelective(buyUserBill);
    }

    /**
     * 获取上级代理
     * @param userId
     * @param level
     * @return
     */
    private List<SuUserEntity> getParentUser(Integer userId, Integer level){
        List<SuUserEntity> result = new ArrayList<>();
        Integer parentId = suUserMapper.selectByPrimaryKey(userId).getParentId();
        for (int i = 0; i < level; i++) {
            if(parentId == null || parentId.equals(""))
                return result;
            SuUserEntity parentUser = suUserMapper.selectByPrimaryKey(parentId);
            result.add(parentUser);
            parentId = parentUser.getParentId();
        }
        return result;
    }

    /**
     * 通过订单和费率生成账单
     * @param userId        :账单所属用户
     * @param order
     * @param rate
     * @return
     */
    private SuBillEntity createOrderBill(Integer userId,SuOrderEntity order,Integer rate){
        SuBillEntity bill = new SuBillEntity();
        bill.setUserId(order.getUserId());
        bill.setMoney(PromotionUtils.calcByRate(order.getPromotionAmount(),rate));
        bill.setType(order.getType());
        bill.setOrderId(order.getId());
        bill.setPromotionRate(rate);
        bill.setState(BillTypeConstant.ORDER);
        bill.setFailReason(order.getFailReason());
        bill.setUpdateTime(new Date());
        bill.setCreateTime(new Date());
        return bill;
    }

    @LcnTransaction
    @Transactional
    @Override
    public void payOutOrderBill(SuOrderEntity order) {
        List<SuBillEntity> suBillEntities = getOrderRelatedBill(order);
        suBillEntities.stream().forEach(o->{
            o.setState(BillStateConstant.READY);
            o.setUpdateTime(new Date());
            suBillMapper.updateByPrimaryKeySelective(o);
            rabbitTemplate.convertAndSend(BillMqConfig.EXCHANGE,BillMqConfig.KEY,o);
        });
    }

    @LcnTransaction
    @Transactional
    @Override
    public void invalidOrderBill(SuOrderEntity order) {
        List<SuBillEntity> suBillEntities = getOrderRelatedBill(order);
        suBillEntities.stream().forEach(o->{
            o.setState(BillStateConstant.FAIL);
            o.setFailReason(order.getFailReason());
            o.setUpdateTime(new Date());
            suBillMapper.updateByPrimaryKeySelective(o);
        });
    }

    /**
     * 获取订单相关账单
     * @param suOrderEntity
     * @return
     */
    private List<SuBillEntity> getOrderRelatedBill(SuOrderEntity suOrderEntity){
        SuBillEntity example = new SuBillEntity();
        example.setOrderId(suOrderEntity.getId());
        return suBillMapper.select(example);
    }
}
