package com.xm.comment.aspect;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xm.comment.annotation.AppType;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.annotation.Pid;
import com.xm.comment.annotation.PlatformType;
import com.xm.comment_feign.module.mall.feign.MallFeignClient;
import com.xm.comment_feign.module.user.feign.UserFeignClient;
import com.xm.comment_serialize.form.BaseForm;
import com.xm.comment_serialize.module.gateway.constant.RequestHeaderConstant;
import com.xm.comment_serialize.module.mall.constant.PlatformTypeConstant;
import com.xm.comment_serialize.module.user.entity.SuPidEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
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
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Component
@Aspect
public class BaseFormAspect {

    @Autowired
    private UserFeignClient userFeignClient;

    @Pointcut("execution(public * com.xm.*.controller.*.*(..,(com.xm.comment_serialize.form.BaseForm+),..))")
    public void pointCut(){}

    @Around("pointCut()")
    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Parameter[] parameters = targetMethod.getParameters();
        Integer index = null;
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        for (int i = 0; i < parameters.length; i++) {
            if(!(joinPoint.getArgs()[i] instanceof BaseForm))
                continue;
            index = i;
            BaseForm baseForm = (BaseForm)joinPoint.getArgs()[index];
            if(baseForm == null)
                baseForm = (BaseForm)parameters[i].getType().newInstance();

            //装配真实IP
            String ip = request.getHeader(RequestHeaderConstant.IP);
            baseForm.setIp(ip);

            //装配userId
            if(baseForm.getUserId() == null){
                Annotation annotation = parameters[i].getAnnotation(LoginUser.class);
                String userId = request.getHeader(RequestHeaderConstant.USER_ID);
                SuUserEntity suUserEntity = JSON.parseObject(Base64.decodeStr(request.getHeader(RequestHeaderConstant.USER_INFO)),SuUserEntity.class);

                if(annotation != null && ((LoginUser)annotation).necessary() && StrUtil.isBlank(userId)){
                    throw new GlobleException(MsgEnum.SYSTEM_INVALID_USER_ERROR);
                }else {
                    baseForm.setUserId(userId != null ? Integer.valueOf(userId) : null);
                    baseForm.setOpenId(suUserEntity != null ? suUserEntity.getOpenId() : null);
                }
            }

            //装配platformType
            if(baseForm.getPlatformType() == null){
                Annotation annotation = parameters[i].getAnnotation(PlatformType.class);
                String platformType = request.getHeader(RequestHeaderConstant.PLATFORM_TYPE);
                if(annotation != null && ((PlatformType)annotation).necessary() && StrUtil.isBlank(platformType)){
                    throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"platformType 不存在");
                }else {
                    baseForm.setPlatformType(platformType != null ? Integer.valueOf(platformType) : null);
                }
            }

            //装配appType
            if(baseForm.getAppType() == null){
                Annotation annotation = parameters[i].getAnnotation(AppType.class);
                String appType = request.getHeader(RequestHeaderConstant.APP_TYPE);
                if(annotation != null && ((AppType)annotation).necessary() && StrUtil.isBlank(appType)){
                    throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"appType 不存在");
                }else {
                    baseForm.setAppType(appType != null ? Integer.valueOf(appType) : null);
                }
            }

            //装配pid
            if(baseForm.getPid() == null){
                Annotation annotation = parameters[i].getAnnotation(Pid.class);
                if(annotation != null && ((Pid)annotation).necessary() && baseForm.getUserId() == null){
                    throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"pid 获取失败 userId不存在");
                }else if(annotation != null && ((Pid)annotation).necessary() && baseForm.getUserId() != null){
                    String userInfo = request.getHeader(RequestHeaderConstant.USER_INFO);
                    SuUserEntity suUserEntity = JSON.parseObject(Base64.decodeStr(userInfo),SuUserEntity.class);
                    SuPidEntity suPidEntity = userFeignClient.getPid(suUserEntity.getPid());
                    String pid = null;
                    switch (Integer.valueOf(baseForm.getPlatformType())){
                        case PlatformTypeConstant.PDD:{
                            pid = suPidEntity.getPdd();
                            break;
                        }
                        case PlatformTypeConstant.JD:{
                            pid = suPidEntity.getJd();
                            break;
                        }
                        case PlatformTypeConstant.MGJ:{
                            pid = suPidEntity.getMgj();
                            break;
                        }
                        case PlatformTypeConstant.TB: {
                            pid = suPidEntity.getTb();
                            break;
                        }
                        case PlatformTypeConstant.WPH:{
                            pid = suPidEntity.getWph();
                            break;
                        }
                    }
                    baseForm.setPid(pid);
                }
            }

            joinPoint.getArgs()[index] = baseForm;
            break;
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }
}
