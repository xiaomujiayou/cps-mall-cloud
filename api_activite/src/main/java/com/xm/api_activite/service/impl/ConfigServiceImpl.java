package com.xm.api_activite.service.impl;

import com.xm.api_activite.mapper.SaConfigMapper;
import com.xm.api_activite.service.ConfigService;
import com.xm.comment_serialize.module.active.entity.SaConfigEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("configService")
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private SaConfigMapper saConfigMapper;

    @Override
    public SaConfigEntity getConfig(String name) {
        SaConfigEntity configEntity = new SaConfigEntity();
        configEntity.setName(name);
        return saConfigMapper.selectOne(configEntity);
    }
}
