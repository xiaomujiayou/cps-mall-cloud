package com.xm.comment.aspect;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.module.user.feign.UserFeignClient;
import com.xm.comment.response.MsgEnum;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.vo.UserInfoVo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;

/**
 * 获取登录的用户信息
 */

@Component
@Aspect
public class LoginUserAspect {

    @Autowired
    private UserFeignClient userFeignClient;

    @Pointcut("execution(public * com.xm.*.controller.*.*(..,@com.xm.comment.annotation.LoginUser (*),..))")
    public void pointCut(){}


    @Around("pointCut()")
    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Parameter[] parameters = targetMethod.getParameters();
        boolean annotationFlag = false;
        boolean isObjInfo = false;
        Integer index = null;
        LoginUser annotation = null;
        for (Parameter parameter:parameters){
            annotation = parameter.getAnnotation(LoginUser.class);
            if(annotation != null) {
                annotationFlag = true;
                index = ArrayUtils.indexOf(parameters,parameter);
                if(!parameter.getType().equals(Integer.class))
                    isObjInfo = true;
                break;
            }
        }
        if(!annotationFlag || joinPoint.getArgs()[index] != null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userId = request.getHeader("user-id");
        String userInfo = request.getHeader("user-info");

        if(annotation.necessary() && StrUtil.hasBlank(userId,userId))
            return R.error(MsgEnum.SYSTEM_INVALID_USER_ERROR);

        if(isObjInfo && StrUtil.isNotBlank(userInfo)){
            if(annotation.latest()){
                SuUserEntity suUserEntity = userFeignClient.getInfoDetail(Integer.valueOf(userId)).getData();
                joinPoint.getArgs()[index] = suUserEntity;
            }else {
                SuUserEntity suUserEntity = JSON.parseObject(Base64.decodeStr(userInfo),SuUserEntity.class);
                joinPoint.getArgs()[index] = suUserEntity;
            }
        }else if(!isObjInfo && StrUtil.isNotBlank(userId)){
            joinPoint.getArgs()[index] = Integer.valueOf(userId);
        }else {
            return joinPoint.proceed();
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }
}
