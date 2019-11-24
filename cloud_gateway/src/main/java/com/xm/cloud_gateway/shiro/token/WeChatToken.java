package com.xm.cloud_gateway.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

public class WeChatToken implements AuthenticationToken {

    private String openId;

    public WeChatToken(String openId) {
        this.openId = openId;
    }

    @Override
    public Object getPrincipal() {
        return openId;
    }

    @Override
    public Object getCredentials() {
        return "";
    }


}
