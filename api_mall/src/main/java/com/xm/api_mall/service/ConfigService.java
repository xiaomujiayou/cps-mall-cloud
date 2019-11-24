package com.xm.api_mall.service;

import com.xm.comment_serialize.module.mall.constant.ConfigEnmu;
import com.xm.comment_serialize.module.mall.entity.SmConfigEntity;

import java.util.List;

/**
 * 配置服务
 */
public interface ConfigService {

    /**
     * 获取某种配置
     * 配置不存在则获取系统配置
     * @param configEnmu
     * @return
     */
    public SmConfigEntity getConfig(Integer userId,ConfigEnmu configEnmu,int configType);

    /**
     * 获取用户所有配置信息
     * 配置不存在则获取系统配置
     * @param userId
     * @return
     */
    public List<SmConfigEntity> getAllConfig(Integer userId,int configType);
}
