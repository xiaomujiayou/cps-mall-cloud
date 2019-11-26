package com.xm.api_user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.xm.api_user.mapper.SuBillMapper;
import com.xm.api_user.service.BillService;
import com.xm.comment.module.mall.feign.MallFeignClient;
import com.xm.comment_mq.config.BillMqConfig;
import com.xm.comment_serialize.module.mall.constant.ConfigEnmu;
import com.xm.comment_serialize.module.mall.constant.ConfigTypeConstant;
import com.xm.comment_serialize.module.user.constant.BillStateConstant;
import com.xm.comment_serialize.module.user.constant.BillTypeConstant;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_utils.project.PromotionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("billService")
public class BillServiceImpl implements BillService {

    @Autowired
    private SuBillMapper suBillMapper;
    @Autowired
    private MallFeignClient mallFeignClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @LcnTransaction
    @Transactional
    @Override
    public void createByOrder(SuOrderEntity order) {
        //订单相关账单存在则返回
        if(getOrderRelatedBill(order) != null)
            return;
        JSONObject params = JSON.parseObject(order.getCustomParameters());
        Integer shareUserId = params.getInteger("fromUser");
        if(shareUserId != null){
            //属于分享订单
            //生成分享者账单
            Integer shareUserRate = Integer.valueOf(mallFeignClient.getOneConfig(
                    shareUserId,
                    ConfigEnmu.PRODUCT_SHARE_USER_RATE.getName(),
                    ConfigTypeConstant.SYS_CONFIG).getData().getVal());

            SuBillEntity shareUserBill = createBill(
                    shareUserId,
                    PromotionUtils.calcByRate(order.getPromotionAmount(),shareUserRate),
                    shareUserRate,
                    BillTypeConstant.ORDER,
                    order.getId(),
                    BillStateConstant.WAIT,
                    order.getFailReason());
            suBillMapper.insertSelective(shareUserBill);

            //生成购买者订单
            Integer buyUserRate = Integer.valueOf(mallFeignClient.getOneConfig(
                    shareUserId,
                    ConfigEnmu.PRODUCT_SHARE_USER_RATE.getName(),
                    ConfigTypeConstant.SYS_CONFIG).getData().getVal());
            SuBillEntity buyUserBill = createBill(
                    order.getUserId(),
                    PromotionUtils.calcByRate(order.getPromotionAmount(),buyUserRate),
                    buyUserRate,
                    BillTypeConstant.ORDER,
                    order.getId(),
                    BillStateConstant.WAIT,
                    order.getFailReason());
            suBillMapper.insertSelective(buyUserBill);
        }

    }
    private SuBillEntity createBill(Integer userId,Integer money,Integer type,Integer orderId,Integer promotionRate,Integer state,String failReason){
        SuBillEntity shareBill = new SuBillEntity();
        shareBill.setUserId(userId);
        shareBill.setMoney(money);
        shareBill.setType(type);
        shareBill.setOrderId(orderId);
        shareBill.setPromotionRate(promotionRate);
        shareBill.setState(state);
        shareBill.setFailReason(failReason);
        shareBill.setUpdateTime(new Date());
        shareBill.setCreateTime(new Date());
        return shareBill;
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
