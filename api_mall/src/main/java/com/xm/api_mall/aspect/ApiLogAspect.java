package com.xm.api_mall.aspect;

import com.alibaba.fastjson.JSON;
import com.pdd.pop.sdk.http.PopBaseHttpRequest;
import com.pdd.pop.sdk.http.PopBaseHttpResponse;
import com.xm.api_mall.exception.ApiCallException;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.response.MsgEnum;
import com.xm.comment.response.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class ApiLogAspect {
    @Pointcut("execution(public * com.pdd.pop.sdk.http.PopHttpClient.*(*))")
    public void pointCut(){}

    @Around("pointCut()")
    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Parameter[] parameters = targetMethod.getParameters();
        boolean annotationFlag = false;
        Integer index = null;

        Map<String,String> param = null;
        for (Object arg : joinPoint.getArgs()) {
            if(arg instanceof  PopBaseHttpRequest){
                PopBaseHttpRequest request = (PopBaseHttpRequest)arg;
                param = request.getParamsMap();
                break;
            }
        }
        String type = param.get("type");
        PopBaseHttpResponse res = (PopBaseHttpResponse)joinPoint.proceed();
        if(res.getErrorResponse() != null)
            throw new ApiCallException("pdd",type,res.getErrorResponse().getErrorCode().toString(),res.getErrorResponse().getErrorMsg(),param,res);
        param.remove("version");
        param.remove("type");
        param.remove("data_type");
        log.debug("pdd api:[{}] param:[{}] res:[{}]",type,JSON.toJSONString(param),JSON.toJSONString(res));
        return res;
    }
}

