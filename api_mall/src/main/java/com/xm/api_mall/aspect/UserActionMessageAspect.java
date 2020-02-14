package com.xm.api_mall.aspect;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.pdd.pop.sdk.http.PopBaseHttpRequest;
import com.pdd.pop.sdk.http.PopBaseHttpResponse;
import com.xm.api_mall.exception.ApiCallException;
import com.xm.api_mall.utils.TextToGoodsUtils;
import com.xm.comment_mq.config.UserActionConfig;
import com.xm.comment_mq.message.impl.UserSearchGoodsMessage;
import com.xm.comment_mq.message.impl.UserShareGoodsMessage;
import com.xm.comment_mq.message.impl.UserSmartSearchGoodsMessage;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.ProductListForm;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.comment_utils.response.Msg;
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
import java.util.Map;

/**
 * 生成用户动作mq消息
 */
@Aspect
@Component
@Slf4j
public class UserActionMessageAspect {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Pointcut("execution(public * com.xm.api_mall.service.ProductService.detail(..))")
    public void goodsDetailMessagePointCut(){}

    /**
     * 生成
     * UserShareGoodsMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("goodsDetailMessagePointCut()")
    public Object goodsDetailMessagePointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        SmProductEntityEx smProductEntityEx = (SmProductEntityEx)joinPoint.proceed();
        Integer userId = (Integer) joinPoint.getArgs()[2];
        Integer shareUserId = (Integer) joinPoint.getArgs()[3];
        if(shareUserId != null && userId != null && !shareUserId.equals(userId))
            rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserShareGoodsMessage(shareUserId,userId,smProductEntityEx));
        return smProductEntityEx;
    }


    @Pointcut("execution(public * com.xm.api_mall.service.ProductService.keywordList(..))")
    public void keywordListPointCut(){}
    /**
     * 生成
     * UserSearchGoodsMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("keywordListPointCut()")
    public Object keywordListPointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        PageBean<SmProductEntityEx> smProductEntityExPageBean = (PageBean<SmProductEntityEx>)joinPoint.proceed();
        Integer userId = (Integer) joinPoint.getArgs()[0];
        ProductListForm productListForm = (ProductListForm) joinPoint.getArgs()[2];
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserSearchGoodsMessage(userId,productListForm.getPlatformType(),productListForm.getKeywords(),productListForm.getPageNum()));
        return smProductEntityExPageBean;
    }

    @Pointcut("execution(public * com.xm.api_mall.controller.ProductController.parseUrl(..))")
    public void parseUrlPointCut(){}
    /**
     * 生成
     * UserSmartSearchGoodsMessage
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("parseUrlPointCut()")
    public Object parseUrlPointCut(ProceedingJoinPoint joinPoint) throws Throwable{
        TextToGoodsUtils.GoodsSpec goodsSpecMsg = (TextToGoodsUtils.GoodsSpec)joinPoint.proceed();
        SmProductEntityEx smProductEntity = new SmProductEntityEx();
        BeanUtil.copyProperties(goodsSpecMsg,smProductEntity);
        Integer userId = (Integer) joinPoint.getArgs()[0];
        String url = (String) joinPoint.getArgs()[2];
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new UserSmartSearchGoodsMessage(userId,smProductEntity,url,smProductEntity.getName()));
        return goodsSpecMsg;
    }
}

