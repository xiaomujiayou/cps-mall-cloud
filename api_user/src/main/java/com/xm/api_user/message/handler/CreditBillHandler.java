package com.xm.api_user.message.handler;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.xm.api_user.mapper.SuBillMapper;
import com.xm.api_user.service.BillService;
import com.xm.comment.utils.LockHelper;
import com.xm.comment_mq.handler.MessageHandler;
import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.impl.*;
import com.xm.comment_serialize.module.user.constant.BillStateConstant;
import com.xm.comment_serialize.module.user.constant.BillTypeConstant;
import com.xm.comment_serialize.module.user.constant.OrderStateConstant;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_serialize.module.wind.constant.CreditBillUnbindConstant;
import com.xm.comment_serialize.module.wind.entity.SwCreditBillPayRecordEntity;
import com.xm.comment_serialize.module.wind.entity.SwCreditRecordEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 设置账单信用支付状态
 */
@Slf4j
@Component
public class CreditBillHandler implements MessageHandler {

    @Autowired
    private SuBillMapper suBillMapper;
    @Autowired
    private BillService billService;

    @Override
    public List<Class> getType() {
        return Lists.newArrayList(
                UserCreditPayBillCreateMessage.class,
                UserCreditBillCountDownMessage.class,
                UserUnBindCreditBillMessage.class
        );
    }

    @Override
    public void handle(AbsUserActionMessage message) {
        if(message instanceof UserCreditPayBillCreateMessage) {
            /**
             * 信用账单已建立
             */
            UserCreditPayBillCreateMessage creditPayBillCreateMessage = (UserCreditPayBillCreateMessage) message;
            //重复检测
            SuBillEntity record = creditPayBillCreateMessage.getSuBillEntity();
            if (record == null || record.getId() == null){
                log.warn("UserCreditPayBillCreateMessage 中包含的 SuBillEntity 不存在：message:{}", JSON.toJSONString(creditPayBillCreateMessage));
                return;
            }
            SuBillEntity suBillEntity = suBillMapper.selectByPrimaryKey(creditPayBillCreateMessage.getSuBillEntity().getId());
            for (int i = 0; i < 5; i++) {
                if(suBillEntity == null) {
                    try {
                        Thread.sleep(1000);
                        suBillEntity = suBillMapper.selectByPrimaryKey(creditPayBillCreateMessage.getSuBillEntity().getId());
                        if(suBillEntity != null)
                            break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (suBillEntity == null || suBillEntity.getCreditState() != null){
                log.warn("重复消息 UserCreditPayBillCreateMessage 消息已被处理：message:{}", JSON.toJSONString(creditPayBillCreateMessage));
                return;
            }
            //设置账单信用支付状态
            SwCreditBillPayRecordEntity billPayRecordEntity = creditPayBillCreateMessage.getSwCreditBillPayRecordEntity();
            if (billPayRecordEntity != null) {
                suBillEntity.setCreditState(billPayRecordEntity.getCheckResult());
                suBillMapper.updateByPrimaryKeySelective(suBillEntity);
            }else {
                log.warn("无效 UserCreditPayBillCreateMessage 消息中 SwCreditBillPayRecordEntity 不存在：message:{}", JSON.toJSONString(creditPayBillCreateMessage));
            }
        }else if(message instanceof UserCreditBillCountDownMessage){
            /**
             * 设置信用支付付款时间
             */
            UserCreditBillCountDownMessage creditBillCountDownMessage = (UserCreditBillCountDownMessage)message;
            SuBillEntity suBillEntity = creditBillCountDownMessage.getSuBillEntity();
            SuBillEntity record = suBillMapper.selectByPrimaryKey(suBillEntity.getId());
            if(record.getPayTime() != null)
                return;
            SuBillEntity bill = new SuBillEntity();
            bill.setId(record.getId());
            bill.setPayTime(suBillEntity.getPayTime());
            suBillMapper.updateByPrimaryKeySelective(bill);
        }else if(message instanceof UserUnBindCreditBillMessage){
            /**
             * 信用降低解绑一个账单
             */
            UserUnBindCreditBillMessage unBindCreditBillMessage = (UserUnBindCreditBillMessage)message;
            if(unBindCreditBillMessage.getUnBindType() == CreditBillUnbindConstant.CREDIT_REDUCE){
                //信用降低，信用解绑
                SuBillEntity suBillEntity = new SuBillEntity();
                suBillEntity.setId(unBindCreditBillMessage.getSuBillEntity().getId());
                suBillEntity.setCreditState(2);
                suBillMapper.updateByPrimaryKeySelective(suBillEntity);
            }
        }
    }

    @Override
    public void onError(Exception e) {

    }
}
