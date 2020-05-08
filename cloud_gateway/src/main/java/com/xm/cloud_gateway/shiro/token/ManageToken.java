package com.xm.cloud_gateway.shiro.token;

import com.xm.comment_serialize.module.user.form.AdminLoginForm;
import com.xm.comment_serialize.module.user.vo.SuAdminVo;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 后台登录token
 */
public class ManageToken extends UsernamePasswordToken {
    public ManageToken(String username, String password, boolean rememberMe) {
        super(username, password, rememberMe);
    }

    private SuAdminVo suAdminVo;

    public SuAdminVo getSuAdminVo() {
        return suAdminVo;
    }

    public void setSuAdminVo(SuAdminVo suAdminVo) {
        this.suAdminVo = suAdminVo;
    }
}
