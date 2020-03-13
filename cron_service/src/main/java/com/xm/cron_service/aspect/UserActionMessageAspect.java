package com.xm.cron_service.aspect;

import com.xm.comment_mq.message.config.UserActionConfig;
import com.xm.comment_mq.message.impl.UserPaymentSucessMessage;
import com.xm.comment_serialize.module.cron.entity.ScBillPayEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.ProductListForm;
import com.xm.comment_serialize.module.pay.entity.SpWxEntPayOrderInEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 生成用户动作mq消息
 */
@Aspect
@Component
@Slf4j
public class UserActionMessageAspect {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Pointcut("execution(public * com.xm.cron_service.service.BillPayService.onEntPaySucess(..))")
    public void userPaymentSucessMessagePointCut(){}

    /**
     * 生成
     * UserPaymentSucessMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("userPaymentSucessMessagePointCut()")
    public Object goodsDetailMessagePointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        SmProductEntityEx smProductEntityEx = (SmProductEntityEx)joinPoint.proceed();
        ScBillPayEntity scBillPayEntity = (ScBillPayEntity) joinPoint.getArgs()[0];
        SpWxEntPayOrderInEntity spWxEntPayOrderInEntity = (SpWxEntPayOrderInEntity) joinPoint.getArgs()[1];
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserPaymentSucessMessage(scBillPayEntity.getUserId(),scBillPayEntity,spWxEntPayOrderInEntity));
        return smProductEntityEx;
    }



}

