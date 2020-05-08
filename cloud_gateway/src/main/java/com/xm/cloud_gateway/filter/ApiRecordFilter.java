package com.xm.cloud_gateway.filter;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xm.cloud_gateway.util.IpUtil;
import com.xm.comment.aspect.ExecTimeAspect;
import com.xm.comment_mq.message.config.WindMqConfig;
import com.xm.comment_serialize.module.gateway.constant.RequestHeaderConstant;
import com.xm.comment_serialize.module.wind.entity.SwApiRecordEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;

/**
 * 风控系统
 * 记录用户的api请求
 */
@Slf4j
@Component
public class ApiRecordFilter extends ZuulFilter {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_RESPONSE_FILTER_ORDER  - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        SwApiRecordEntity entity = new SwApiRecordEntity();
        //设置userId
        String userId = getUserId();
        entity.setUserId(userId != null ? Integer.valueOf(userId) : null);
        //设置appType
        if(request.getRequestURL().toString().contains("/manage/")){
            entity.setAppType(9);
        }else {
            String appType = getHeader(RequestHeaderConstant.APP_TYPE);
            entity.setAppType(appType != null ? Integer.valueOf(appType) : null);
        }
        //设置IP
        entity.setIp(IpUtil.getIp(request));
        //设置URI
        entity.setUrl(request.getRequestURI());
        //设置method
        entity.setMethod(request.getMethod());
        //设置参数
        if("GET".equals(request.getMethod())){
            Map<String,Object> params = new HashMap<>();
            Enumeration<String> enumeration = request.getParameterNames();
            while (enumeration.hasMoreElements()){
                String paramName = enumeration.nextElement();
                params.put(paramName,request.getParameter(paramName));
            }
            entity.setParam(params.isEmpty() ? null : JSON.toJSONString(params));
        }else if("POST".equals(request.getMethod())){
            String requestBody = null;
            try {
                requestBody = IoUtil.read(request.getInputStream(),"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            entity.setParam(requestBody);
        }

        //设置执行时间
        AtomicBoolean should = new AtomicBoolean(false);
        String[] execTime = {null};
        context.getZuulResponseHeaders().stream().forEach(o ->{
            if(o.first().equals("Content-Type"))
                should.set(o.second().contains("application/json"));
            if(o.first().equals(ExecTimeAspect.EXEC_TIME_HEADER))
                execTime[0] = o.first().equals(ExecTimeAspect.EXEC_TIME_HEADER) ? o.second() : null;
        });
        entity.setTime(execTime[0] != null ? Integer.valueOf(execTime[0]) : null);
        //设置结果
        if(should.get()){
            entity.setResult(context.getResponseBody());
        }
        entity.setUa(request.getHeader("User-Agent"));
        entity.setCreateTime(new Date());
        rabbitTemplate.convertAndSend(WindMqConfig.EXCHANGE,WindMqConfig.KEY_API,entity);
        return null;
    }

    private String getHeader(String headerName){
        RequestContext requestContext = RequestContext.getCurrentContext();
        return requestContext.getRequest().getHeader(headerName);
    }
    private String getUserId(){
        RequestContext requestContext = RequestContext.getCurrentContext();
        return requestContext.getZuulRequestHeaders().get(RequestHeaderConstant.USER_ID);
    }
}
