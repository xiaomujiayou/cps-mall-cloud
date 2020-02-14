package com.xm.comment_feign.config;

import feign.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class FeignConfiguration {


    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    /**
     * 增加字符串转日期的功能
     * 解决Date传递时差问题
     */
    @PostConstruct
    public void initEditableValidation() {
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter.getWebBindingInitializer();
        if (initializer.getConversionService() != null) {
            GenericConversionService genericConversionService = (GenericConversionService) initializer.getConversionService();
            genericConversionService.addConverter(String.class, Date.class,new String2DateConverter());
        }
    }

    private class String2DateConverter implements Converter<String, Date> {
        @Override
        public Date convert(String source) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                return simpleDateFormat.parse(source);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}