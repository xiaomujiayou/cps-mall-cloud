package com.xm.comment_feign.config;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xm.comment_utils.enu.EnumUtils;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * feign异常解析
 * 服务间的异常处理，只处理使用feignclient触发的异常
 * 异常处理方案：
 * ①：自定义异常（GlobleException），则直接抛向上级服务
 * ②：系统异常，由于feignclient会丢失原始异常信息只保留message，因此将异常详细信息封装在message中，封装成GlobleException,并且表明该异常为系统异常，抛向上级服务
 * 历史原因，全部使用GlobleException
 */
@Slf4j
@Configuration
public class FeignClientErrorDecoder implements feign.codec.ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        String errorMsg = null;
        try {
            errorMsg = IoUtil.read(response.body().asReader());
            if(errorMsg.startsWith("{") && errorMsg.endsWith("}")){
                JSONObject errJson = JSON.parseObject(errorMsg);
                //从message中解析出自定义异常
                if(errJson.getString("message").startsWith("{")){
                    //自定义GlobleException
                    try {
                        JSONObject message = errJson.getJSONObject("message");
                        Integer code = message.getInteger("code");
                        MsgEnum msgEnum = EnumUtils.getEnum(MsgEnum.class,"code",code);
                        String[] desc = message.getObject("stackInfo",String[].class);
                        String msg = message.getObject("msg",String.class);
                        return new GlobleException(msgEnum,msg,desc);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }else {
                    //系统异常
                    return new GlobleException(errJson.getString("message"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new GlobleException(MsgEnum.UNKNOWN_ERROR,null,errorMsg);
    }
}