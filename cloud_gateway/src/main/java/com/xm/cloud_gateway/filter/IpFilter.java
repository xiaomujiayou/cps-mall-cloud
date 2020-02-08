package com.xm.cloud_gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xm.comment_utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取客户端真实ip
 */
@Slf4j
@Component
public class IpFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return null;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        System.out.println("客户端header:"+ JSON.toJSONString(requestContext.getZuulRequestHeaders()));
        while (request.getHeaderNames().hasMoreElements()){
            String headerName = request.getHeaderNames().nextElement();
            System.out.println(headerName + ":" + request.getHeader(headerName));
        }
        return null;
    }


}
