package com.xm.cloud_gateway.config;


import com.xm.cloud_gateway.shiro.realm.CustomRealm;
import com.xm.comment.module.user.feign.UserFeignClient;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

//@Configuration
public class ShiroConf {
//    @Bean(name = "customRealm")
//    public CustomRealm customRealm() {
//        return new CustomRealm();
//    }

//    @Autowired
//    private UserFeignClient userFeignClient;
//
//    @Bean
//    public CustomRealm getCustomRealm(){
//        return new CustomRealm();
//    }
//
//    @Bean(name = "securityManager")
//    public DefaultWebSecurityManager defaultWebSecurityManager() {
//        System.out.println(userFeignClient);
//        DefaultWebSecurityManager  securityManager = new DefaultWebSecurityManager ();
////        securityManager.setRealm(customRealm);
//        return securityManager;
//    }
//
//    @Bean
//    ShiroFilterChainDefinition shiroFilterChainDefinition() {
//        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
//        definition.addPathDefinition("/doLogin", "anon");
//        definition.addPathDefinition("/**", "authc");
//        return definition;
//    }
}
