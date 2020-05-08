package com.xm.cloud_gateway.shiro.realm;

import com.xm.cloud_gateway.shiro.token.ManageToken;
import com.xm.cloud_gateway.shiro.token.WeChatToken;
import com.xm.comment_feign.module.user.feign.UserFeignClient;
import com.xm.comment_serialize.module.user.form.AdminLoginForm;
import com.xm.comment_serialize.module.user.vo.SuAdminVo;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ManageRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserFeignClient userFeignClient;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        ManageToken manageToken = (ManageToken)token;
        AdminLoginForm adminLoginForm = new AdminLoginForm();
        adminLoginForm.setUserName(manageToken.getUsername());
        adminLoginForm.setPassword(String.valueOf(manageToken.getPassword()));
        SuAdminVo suAdminVo = userFeignClient.adminInfo(adminLoginForm);
        manageToken.setSuAdminVo(suAdminVo);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(manageToken,suAdminVo.getId() != null?manageToken.getPassword():"",getName());
        return authenticationInfo;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof ManageToken;
    }
}
