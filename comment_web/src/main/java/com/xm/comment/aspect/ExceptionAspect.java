//package com.xm.comment.aspect;
//
//import cn.hutool.core.codec.Base64;
//import cn.hutool.core.io.IoUtil;
//import cn.hutool.core.utils.StrUtil;
//import com.alibaba.fastjson.JSON;
//import com.xm.comment.annotation.LoginUser;
//import com.xm.comment_feign.module.user.feign.UserFeignClient;
//import com.xm.comment_serialize.module.user.entity.SuUserEntity;
//import com.xm.comment_utils.exception.GlobleException;
//import com.xm.comment_utils.response.Msg;
//import com.xm.comment_utils.response.MsgEnum;
//import com.xm.comment_utils.response.R;
//import org.apache.commons.lang3.ArrayUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.Ordered;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.lang.reflect.Method;
//import java.lang.reflect.Parameter;
//import java.nio.charset.Charset;
//
///**
// * 异常处理
// * 只处理zuul -> service 之间的异常
// * service -> service 之间的异常使用 FeignClientErrorDecoder
// * 包装异常错误信息到zuul
// */
//
////@Component
////@Aspect
//public class ExceptionAspect {
//
//    @Pointcut("execution(public * com.xm.*.controller.*.*(..))")
//    public void pointCut(){}
//
//
//    @Around("pointCut()")
//    public Object valid(ProceedingJoinPoint joinPoint) throws Throwable{
//        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        HttpServletResponse response =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//        String from = request.getHeader("from");
//
//
//        if(from != null && from.equals("zuul")){
//            //请求来自zuul
//            try{
//                Object result = joinPoint.proceed();
//                System.out.println(request.getRequestURL()+":"+from+":"+JSON.toJSONString(result));
//                return result;
//            }catch (Exception e){
//                //对异常进行包装
//                response.setContentType("application/json");
//                if(e instanceof GlobleException){
//                    GlobleException ge = (GlobleException)e;
//                    MsgEnum msgEnum = ge.getMsgEnum();
//                    if(msgEnum == null)
//                        msgEnum = MsgEnum.UNKNOWN_ERROR;
//                    Msg msg = R.error(msgEnum);
//                    if(ge.getMsgEnum() != null && StrUtil.isNotBlank(ge.getMsg()))
//                        msg.setMsg(ge.getMsg());
//                    IoUtil.write(response.getOutputStream(), Charset.forName("UTF-8"),true,JSON.toJSONString(msg));
//                }else {
//                    //系统异常
//                    IoUtil.write(response.getOutputStream(), Charset.forName("UTF-8"),true,JSON.toJSONString(R.error(MsgEnum.UNKNOWN_ERROR)));
//                }
//                throw e;
//            }
//        }
//        return joinPoint.proceed();
//    }
//
//}
