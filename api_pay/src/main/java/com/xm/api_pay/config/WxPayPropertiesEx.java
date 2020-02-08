package com.xm.api_pay.config;

import com.binarywang.spring.starter.wxjava.pay.properties.WxPayProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayPropertiesEx extends WxPayProperties {
    private String notifyUrl;
    private String signKey;
}
