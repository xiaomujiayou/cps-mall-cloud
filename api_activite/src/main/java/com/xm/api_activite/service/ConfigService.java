package com.xm.api_activite.service;

import com.xm.comment_serialize.module.active.entity.SaConfigEntity;

public interface ConfigService {
    /**
     * 获取配置
     * @param name
     * @return
     */
    SaConfigEntity getConfig(String name);
}
