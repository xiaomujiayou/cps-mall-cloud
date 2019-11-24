package com.xm.comment.aspect;

import com.xm.comment.annotation.LoginUser;
import com.xm.comment.response.MsgEnum;
import com.xm.comment.response.R;
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
import java.util.Date;

/**
 * 获取登录的用户信息
 */

@Component
@Aspect
public class LoginUserValidAspect {

    @Pointcut("execution(public * com.xm.*.controller.*.*(..,@com.xm.comment.annotation.LoginUser (*),..))")
    public void pointCut(){}

    @Around("pointCut()")
    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Parameter[] parameters = targetMethod.getParameters();
        boolean annotationFlag = false;
        Integer index = null;
        LoginUser annotation = null;
        for (Parameter parameter:parameters){
            annotation = parameter.getAnnotation(LoginUser.class);
            if(annotation != null) {
                annotationFlag = true;
                index = ArrayUtils.indexOf(parameters,parameter);
                break;
            }
        }
        if(!annotationFlag || joinPoint.getArgs()[index] != null) {
            return joinPoint.proceed();
        }
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userId = request.getHeader(annotation.value());
        if(!StringUtils.isBlank(userId)) {
            joinPoint.getArgs()[index] = Integer.valueOf(userId);
            return joinPoint.proceed(joinPoint.getArgs());
        }

//        for (Object arg:joinPoint.getArgs()){
//            if(arg instanceof HttpServletRequest){
//                HttpServletRequest request = (HttpServletRequest)arg;
//                String userId = request.getHeader(annotation.value());
//                if(StringUtils.isBlank(userId)) {
//                    break;
//                }
//                joinPoint.getArgs()[index] = Integer.valueOf(request.getHeader(annotation.value()));
//
//                return joinPoint.proceed(joinPoint.getArgs());
//            }
//        }
        if(annotation.necessary()){
            return R.error(MsgEnum.SYSTEM_INVALID_USER_ERROR);
        }
        return joinPoint.proceed();
    }
}
