package com.xm.cloud_gateway.filter;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xm.comment.constants.TokenConstants;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 用户信息过滤器
 * 如果用户已登录并且存在，则将用户信息设置到request header中，以便其他服务通过@LoginUser注解获取
 */
@Slf4j
@Component
public class UserInfoFilter extends ZuulFilter {

    //拦截白名单
//    private static String[] whiteList = new String[]{"/user/register","/user/login"};

//    @Autowired
//    private StringRedisTemplate redisTemplate;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER -1;
    }

    @Override
    public boolean shouldFilter() {
//        RequestContext requestContext = RequestContext.getCurrentContext();
//        HttpServletRequest request = requestContext.getRequest();
//        String url = request.getRequestURI();
//        for (String s : whiteList) {
//            if(url.contains(s)){
//                log.debug("命中白名单链接："+url);
//                return false;
//            }
//        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String token = request.getHeader("token");

        if(StrUtil.isBlank(token))
            return null;

        Session session = null;
        try {
            session = SecurityUtils.getSecurityManager().getSession(new MySessionKey(token));
            if((Boolean) session.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY)){
                Object user = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                SimplePrincipalCollection principal = (SimplePrincipalCollection)user;
                SuUserEntity suUserEntity = principal.oneByType(SuUserEntity.class);
                System.out.println(principal);
                if(user != null){
                    requestContext.addZuulRequestHeader("user-info", Base64.encode(JSON.toJSONString(suUserEntity)));
                    requestContext.addZuulRequestHeader("user-id", suUserEntity.getId().toString());
                }
            }

        }catch (UnknownSessionException e){
            log.debug("无效 token：{}",token);
        }
        return null;
    }
    public class MySessionKey implements SessionKey {

        private String token;

        public MySessionKey(String token) {
            this.token = token;
        }

        @Override
        public Serializable getSessionId() {
            return token;
        }
    }
}
