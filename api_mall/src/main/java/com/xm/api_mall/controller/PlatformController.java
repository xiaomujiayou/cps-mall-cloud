package com.xm.api_mall.controller;

import cn.hutool.core.map.MapUtil;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.module.mall.feign.MallFeignClient;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.R;
import com.xm.comment_serialize.module.mall.constant.ConfigEnmu;
import com.xm.comment_serialize.module.mall.constant.ConfigTypeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/platform")
public class PlatformController {

    @Autowired
    private MallFeignClient mallFeignClient;

    /**
     *  平台按钮配置
     * @param userId
     * @return
     */
    @GetMapping("/config")
    public Msg<Map<String,Object>> config(@LoginUser Integer userId){
        Map<String,Object> result = new HashMap<>();
        String isOn = mallFeignClient.getOneConfig(userId, ConfigEnmu.PLATFORM_CHOOSE_BTN.getName(), ConfigTypeConstant.PROXY_CONFIG).getData().getVal();
        result.put("isOn",isOn.equals("1")?true:false);
        return R.sucess(result);
    }

    /**
     * 获取平台列表
     * @param userId
     * @return
     */
    @GetMapping
    public Msg<Object> get(@LoginUser Integer userId){
        String[] platformNames = mallFeignClient.getOneConfig(userId, ConfigEnmu.PLATFORM_NAME.getName(), ConfigTypeConstant.PROXY_CONFIG).getData().getVal().split(",");
        String[] platformIds = mallFeignClient.getOneConfig(userId, ConfigEnmu.PLATFORM_ID.getName(), ConfigTypeConstant.PROXY_CONFIG).getData().getVal().split(",");
        List<Map<String,Object>> result = IntStream
                .range(0,platformIds.length)
                .boxed()
                .collect(Collectors
                        .toMap(i->platformIds[i],i->platformNames[i]))
                .entrySet()
                .stream()
                .map(o-> MapUtil.builder("type",(Object) Integer.valueOf(o.getKey()))
                        .put("name",o.getValue())
                        .build())
                .collect(Collectors.toList());

        return R.sucess(result);
    }

}
