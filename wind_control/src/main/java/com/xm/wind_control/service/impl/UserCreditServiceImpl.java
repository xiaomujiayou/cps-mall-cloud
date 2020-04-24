package com.xm.wind_control.service.impl;

import cn.hutool.core.date.DateUtil;
import com.xm.comment.utils.LockHelper;
import com.xm.comment_mq.message.config.UserActionConfig;
import com.xm.comment_mq.message.impl.UserCreditChangeMessage;
import com.xm.comment_mq.message.impl.UserUnBindCreditBillMessage;
import com.xm.comment_serialize.module.wind.constant.ChangeCreditEnum;
import com.xm.comment_serialize.module.wind.entity.SwCreditRecordEntity;
import com.xm.comment_utils.response.R;
import com.xm.wind_control.mapper.SwCreditRecordMapper;
import com.xm.wind_control.service.CreditBillService;
import com.xm.wind_control.service.UserCreditService;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.Date;
import java.util.concurrent.locks.Lock;

@Service("userCreditService")
public class UserCreditServiceImpl implements UserCreditService {

    @Autowired
    private SwCreditRecordMapper swCreditRecordMapper;
    @Autowired
    private CreditBillService creditBillService;
    @Autowired
    private RedisLockRegistry redisLockRegistry;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeCredit(Integer userId,ChangeCreditEnum changeCreditEnum,String attached) {
        Lock lock = null;
        try {
            lock = redisLockRegistry.obtain(UserCreditService.class.getSimpleName()+":"+userId);
            lock.lock();
            //判断每天限额
            if(changeCreditEnum.getLimitByDay() != null && changeCreditEnum.getLimitByDay() > 0){
                Date tomorrow = DateUtil.beginOfDay(DateUtil.tomorrow());
                Date beginToday = DateUtil.parse(DateUtil.today());
                Example example = new Example(SwCreditRecordEntity.class);
                example.createCriteria()
                        .andEqualTo("userId",userId)
                        .andEqualTo("type",changeCreditEnum.getType())
                        .andBetween("createTime",beginToday,tomorrow);
                int dayCount = swCreditRecordMapper.selectCountByExample(example);
                if(dayCount >= changeCreditEnum.getLimitByDay())
                    return;
            }
            //判断每月限额
            if(changeCreditEnum.getLimitByMonth() != null && changeCreditEnum.getLimitByMonth() > 0){
                Date now = new Date();
                Date beginMonth = DateUtil.beginOfMonth(now);
                Date endMonth = DateUtil.endOfMonth(now);
                Example example = new Example(SwCreditRecordEntity.class);
                example.createCriteria()
                        .andEqualTo("userId",userId)
                        .andEqualTo("type",changeCreditEnum.getType())
                        .andBetween("createTime",beginMonth,endMonth);
                int monthCount = swCreditRecordMapper.selectCountByExample(example);
                if(monthCount >= changeCreditEnum.getLimitByMonth())
                    return;
            }
            SwCreditRecordEntity userCredit = creditBillService.getUserCredit(userId);
            int scores = userCredit.getScores() + changeCreditEnum.getScores();
            if(scores < 0)
                scores = 0;
            if(scores > 100)
                scores = 100;
            SwCreditRecordEntity userNewCredit = new SwCreditRecordEntity();
            userNewCredit.setUserId(userId);
            userNewCredit.setScores(scores);
            userNewCredit.setAttached(attached);
            userNewCredit.setType(changeCreditEnum.getType());
            userNewCredit.setChangeReason(changeCreditEnum.getName());
            userNewCredit.setChangeScores(changeCreditEnum.getScores());
            Date current = new Date();
            userNewCredit.setCreateStamp(current.getTime()+"");
            userNewCredit.setCreateTime(current);
            swCreditRecordMapper.insertUseGeneratedKeys(userNewCredit);
            rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserCreditChangeMessage(userId,userCredit,userNewCredit,changeCreditEnum));

        }finally {
            if(lock != null)
                lock.unlock();
        }
    }

}
