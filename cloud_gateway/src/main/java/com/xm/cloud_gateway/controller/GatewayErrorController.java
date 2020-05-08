package com.xm.cloud_gateway.controller;

import com.netflix.zuul.exception.ZuulException;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_utils.response.R;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ZuulFilter异常处理
 * 由于ZuulFilter默认异常处理器SendErrorFilter会将异常转发到SpringBoot "/error" 路径上
 * 因此可以重写默认ErrorController，实现异常处理
 */
@RestController
public class GatewayErrorController implements ErrorController {

    /**
     * zuul的异常处理
     * 
     * @param request HTTP请求
     * @return API统一响应
     */
    @RequestMapping
    public Msg error(HttpServletRequest request, HttpServletResponse response) {
        Integer code = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        response.setStatus(HttpStatus.OK.value());
        if(exception instanceof ZuulException){
            ZuulException zuulException = (ZuulException)exception;
            Throwable realException = zuulException.getCause();
            if(realException instanceof GlobleException){
                return R.error(((GlobleException) realException).getMsgEnum());
            }
        }
        return R.error(MsgEnum.UNKNOWN_ERROR);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
