package com.xm.comment.aspect;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xm.comment.annotation.Pid;
import com.xm.comment_feign.module.mall.feign.MallFeignClient;
import com.xm.comment_feign.module.user.feign.UserFeignClient;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_utils.response.R;
import com.xm.comment_serialize.module.mall.constant.PlatformTypeConstant;
import com.xm.comment_serialize.module.mall.entity.SmPidEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import org.apache.commons.lang3.ArrayUtils;
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

/**
 * 获取用户pid
 */
@Component
@Aspect
public class PidAspect {

    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private MallFeignClient mallFeignClient;

    @Pointcut("execution(public * com.xm.*.controller.*.*(..,@com.xm.comment.annotation.Pid (*),..))")
    public void pointCut(){}


    @Around("pointCut()")
    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Parameter[] parameters = targetMethod.getParameters();
        boolean annotationFlag = false;
        Integer index = null;
        Pid annotation = null;
        for (Parameter parameter:parameters){
            annotation = parameter.getAnnotation(Pid.class);
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
        String platformType = request.getHeader("platform-type");
        String userInfo = request.getHeader("user-info");
        if(annotation.necessary() && StrUtil.hasBlank(userInfo,platformType))
            return R.error(MsgEnum.SYSTEM_INVALID_USER_ERROR);

        SuUserEntity suUserEntity = JSON.parseObject(Base64.decodeStr(userInfo),SuUserEntity.class);
        SmPidEntity smPidEntity = mallFeignClient.getPid(suUserEntity.getPid());

        String pid = null;
        switch (Integer.valueOf(platformType)){
            case PlatformTypeConstant.PDD:{
                pid = smPidEntity.getPdd();
                break;
            }
            case PlatformTypeConstant.JD:{
                pid = smPidEntity.getJd();
                break;
            }
            case PlatformTypeConstant.MGJ:{
                pid = smPidEntity.getMgj();
                break;
            }
            case PlatformTypeConstant.TB:{
                pid = smPidEntity.getTb();
                break;
            }
        }
        if(annotation.necessary() && pid == null)
            return R.error(MsgEnum.DATA_ALREADY_NOT_EXISTS,String.format("找不到用户：[%s] 所属平台：[%s]的pid!",suUserEntity.getId(),platformType));
        joinPoint.getArgs()[index] = pid;
        return joinPoint.proceed(joinPoint.getArgs());
    }
}
