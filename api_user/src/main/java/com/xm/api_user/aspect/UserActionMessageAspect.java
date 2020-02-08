package com.xm.api_user.aspect;

import com.xm.api_user.mapper.SuUserMapper;
import com.xm.comment_mq.config.UserActionConfig;
import com.xm.comment_mq.message.impl.*;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 消息生成器
 */
@Aspect
@Component
@Slf4j
public class UserActionMessageAspect {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SuUserMapper suUserMapper;

    @Pointcut("execution(public * com.xm.api_user.service.impl.UserServiceImpl.addNewUser(..))")
    public void addNewUserPointCut(){}

    /**
     * 生成
     * UserFristLoginMessage
     * UserAddProxyMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("addNewUserPointCut()")
    public Object addNewUser(ProceedingJoinPoint joinPoint) throws Throwable{
        SuUserEntity suUserEntity = (SuUserEntity)joinPoint.proceed();
        //首次登录消息
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserFristLoginMessage(suUserEntity.getId(),suUserEntity));
        if(suUserEntity.getParentId() != null) {
            //新增代理消息
            rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserAddProxyMessage(suUserEntity.getParentId(),1, suUserEntity));
            SuUserEntity parentUser = suUserMapper.selectByPrimaryKey(suUserEntity.getParentId());
            if(parentUser.getParentId() != null)
                rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserAddProxyMessage(parentUser.getParentId(),2, suUserEntity));
        }
        return suUserEntity;
    }

    @Pointcut("execution(public * com.xm.api_user.service.OrderService.onOrderCreate(..))")
    public void onOrderCreatePointCut(){}
    /**
     * 生成
     * OrderCreateMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("addNewUserPointCut()")
    public Object onOrderCreatePointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        Object obj = joinPoint.proceed();
        SuOrderEntity suOrderEntity = (SuOrderEntity)joinPoint.getArgs()[0];
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new OrderCreateMessage(suOrderEntity.getUserId(),suOrderEntity));
        return obj;
    }

    @Pointcut("execution(public * com.xm.api_user.service.OrderService.updateOrderState(..))")
    public void updateOrderStatePointCut(){}
    /**
     * 生成
     * OrderStateChangeMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("updateOrderStatePointCut()")
    public Object updateOrderStatePointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        Object obj = joinPoint.proceed();
        SuOrderEntity newOrder = (SuOrderEntity)joinPoint.getArgs()[0];
        SuOrderEntity oldOrder = (SuOrderEntity)joinPoint.getArgs()[1];
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new OrderStateChangeMessage(newOrder.getUserId(),oldOrder,newOrder.getState()));
        return obj;
    }

    @Pointcut("execution(public * com.xm.api_user.service.BillService.payOutOrderBill(..))")
    public void payOutOrderBillPointCut(){}
    /**
     * 生成
     * OrderSettlementSucessMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("payOutOrderBillPointCut()")
    public Object payOutOrderBillPointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        Object obj = joinPoint.proceed();
        SuOrderEntity sucessOrder = (SuOrderEntity)joinPoint.getArgs()[0];
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new OrderSettlementSucessMessage(sucessOrder.getUserId(),sucessOrder));
        return obj;
    }

    @Pointcut("execution(public * com.xm.api_user.service.BillService.invalidOrderBill(..))")
    public void invalidOrderBillPointCut(){}
    /**
     * 生成
     * OrderSettlementFailMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("invalidOrderBillPointCut()")
    public Object invalidOrderBillPointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        Object obj = joinPoint.proceed();
        SuOrderEntity sucessOrder = (SuOrderEntity)joinPoint.getArgs()[0];
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new OrderSettlementFailMessage(sucessOrder.getUserId(),sucessOrder,sucessOrder.getFailReason()));
        return obj;
    }

    @Pointcut("execution(public * com.xm.api_user.service.BillService.addBill(..))")
    public void addBillPointCut(){}
    /**
     * 生成
     * UserBillCreateMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("addBillPointCut()")
    public Object addBillPointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        Object obj = joinPoint.proceed();
        SuBillEntity suBillEntity = (SuBillEntity)joinPoint.getArgs()[0];
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserBillCreateMessage(suBillEntity.getUserId(),suBillEntity));
        return obj;
    }

    @Pointcut("execution(public * com.xm.api_user.service.BillService.updateBillState(..))")
    public void updateBillStatePointCut(){}
    /**
     * 生成
     * UserBillStateChangeMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("updateBillStatePointCut()")
    public Object updateBillStatePointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        Object obj = joinPoint.proceed();
        SuBillEntity suBillEntity = (SuBillEntity)joinPoint.getArgs()[0];
        Integer newState = (Integer)joinPoint.getArgs()[1];
        String failReason = (String) joinPoint.getArgs()[2];
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserBillStateChangeMessage(suBillEntity.getUserId(),suBillEntity,newState,failReason));
        return obj;
    }
}

