package com.xm.comment.aspect;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xm.comment.annotation.AppType;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.annotation.Pid;
import com.xm.comment.annotation.PlatformType;
import com.xm.comment_feign.module.user.feign.UserFeignClient;
import com.xm.comment_serialize.form.BaseForm;
import com.xm.comment_serialize.module.gateway.constant.RequestHeaderConstant;
import com.xm.comment_serialize.module.mall.constant.PlatformTypeConstant;
import com.xm.comment_serialize.module.user.entity.SuPidEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


/**
 *  记录服务响应时间
 *  ApiRecordFilter
 *
 */
@Component
@Aspect
public class ExecTimeAspect {

    public static final String EXEC_TIME_HEADER = "exec-time";

    @Pointcut("execution(public * com.xm.*.controller..*.*(..))")
    public void pointCut(){}

    @Around("pointCut()")
    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable{
        HttpServletResponse response =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        long start = System.currentTimeMillis();
        Object res = joinPoint.proceed();
        response.setHeader(EXEC_TIME_HEADER,(System.currentTimeMillis() - start) + "");
        return res;
    }
}
