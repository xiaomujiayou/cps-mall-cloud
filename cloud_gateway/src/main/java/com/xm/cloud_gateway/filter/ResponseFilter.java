package com.xm.cloud_gateway.filter;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xm.comment_utils.response.R;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_RESPONSE_FILTER_ORDER;

/**
 * 全局数据包装器
 * 包装返回结果,不包括错误信息，错误信息原样输出
 * {data} -> {code:200,msg:"",data:data}
 */
@Component
public class ResponseFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        AtomicBoolean should = new AtomicBoolean(false);
        context.getZuulResponseHeaders().stream().forEach(o ->{
            should.set(o.first().equals("Content-Type") && o.second().contains("application/json"));
        });
        return context.getThrowable() == null
                && should.get();
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        InputStream responseStream = context.getResponseDataStream();
        String resBody = IoUtil.read(responseStream,"UTF-8");
        Object jsonData = JSON.parse(resBody);
        context.getResponse().setCharacterEncoding("UTF-8");
        //结果已被包装，则原样返回(异常)，否则进行包装
        if(jsonData instanceof JSONObject && ((JSONObject)jsonData).getInteger("code") != null ){
            context.setResponseBody(resBody);
            return null;
        }
        resBody = JSON.toJSONString(R.sucess(jsonData));
        context.setResponseBody(resBody);
        return null;
    }
}
