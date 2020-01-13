package com.xm.api_mall.service.impl;

import com.xm.api_mall.mapper.SmConfigMapper;
import com.xm.api_mall.service.ConfigService;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment.module.user.feign.UserFeignClient;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_serialize.module.mall.constant.ConfigEnmu;
import com.xm.comment_serialize.module.mall.constant.ConfigTypeConstant;
import com.xm.comment_serialize.module.mall.entity.SmConfigEntity;
import com.xm.comment_serialize.module.user.constant.UserTypeConstant;
import com.xm.comment_serialize.module.user.entity.SuConfigEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service("configService")
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private SmConfigMapper smConfigMapper;
    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public SmConfigEntity getConfig(Integer userId, ConfigEnmu configEnmu,int configType) {
        if(userId == null || userId == 0){
            Example example = new Example(SmConfigEntity.class);
            example.createCriteria().andEqualTo("name",configEnmu.getName());
            SmConfigEntity smConfigEntity = smConfigMapper.selectOneByExample(example);
            return smConfigEntity;
        }
        switch (configType){
            case ConfigTypeConstant.SYS_CONFIG:{
                Example example = new Example(SmConfigEntity.class);
                example.createCriteria().andEqualTo("name",configEnmu.getName());
                SmConfigEntity smConfigEntity = smConfigMapper.selectOneByExample(example);
                return smConfigEntity;
            }
            case ConfigTypeConstant.SELF_CONFIG:{
                Example example = new Example(SmConfigEntity.class);
                example.createCriteria().andEqualTo("name",configEnmu.getName());
                SmConfigEntity smConfigEntity = smConfigMapper.selectOneByExample(example);
                if(userId == null)
                    return smConfigEntity;
                SuConfigEntity suConfigEntity = userFeignClient.getOneConfig(userId,configEnmu.getName()).getData();
                if(suConfigEntity != null){
                    smConfigEntity.setVal(suConfigEntity.getVal());
                }
                return smConfigEntity;
            }
            case ConfigTypeConstant.PARENT_CONFIG:{
                SuUserEntity parent = userFeignClient.superUser(userId, UserTypeConstant.PARENT).getData();
                if(parent == null){
                    Example example = new Example(SmConfigEntity.class);
                    example.createCriteria().andEqualTo("name",configEnmu.getName());
                    SmConfigEntity smConfigEntity = smConfigMapper.selectOneByExample(example);
                    return smConfigEntity;
                }else {
                    Example example = new Example(SmConfigEntity.class);
                    example.createCriteria().andEqualTo("name",configEnmu.getName());
                    SmConfigEntity smConfigEntity = smConfigMapper.selectOneByExample(example);
                    SuConfigEntity suConfigEntity = userFeignClient.getOneConfig(parent.getId(),configEnmu.getName()).getData();
                    if(suConfigEntity != null){
                        smConfigEntity.setVal(suConfigEntity.getVal());
                    }
                    return smConfigEntity;
                }
            }
            case ConfigTypeConstant.PROXY_CONFIG:{

                Object msg = userFeignClient.superUser(userId, UserTypeConstant.PROXY);
                SuUserEntity proxy = ((Msg<SuUserEntity>)msg).getData();
                if(proxy == null){
                    Example example = new Example(SmConfigEntity.class);
                    example.createCriteria().andEqualTo("name",configEnmu.getName());
                    SmConfigEntity smConfigEntity = smConfigMapper.selectOneByExample(example);
                    return smConfigEntity;
                }else {
                    Example example = new Example(SmConfigEntity.class);
                    example.createCriteria().andEqualTo("name",configEnmu.getName());
                    SmConfigEntity smConfigEntity = smConfigMapper.selectOneByExample(example);
                    SuConfigEntity suConfigEntity = userFeignClient.getOneConfig(proxy.getId(),configEnmu.getName()).getData();
                    if(suConfigEntity != null){
                        smConfigEntity.setVal(suConfigEntity.getVal());
                    }
                    return smConfigEntity;
                }
            }
        }
        throw new GlobleException(MsgEnum.TYPE_NOTFOUND_ERROR);
    }

    @Override
    public List<SmConfigEntity> getAllConfig(Integer userId,int configType) {
        List<SmConfigEntity> smConfigEntitys = smConfigMapper.selectAll();
        switch (configType){
            case ConfigTypeConstant.SYS_CONFIG:{
                return smConfigEntitys;
            }
            case ConfigTypeConstant.SELF_CONFIG:{
                List<SuConfigEntity> suConfigEntities = userFeignClient.getAllConfig(userId).getData();
                if(suConfigEntities == null)
                    return smConfigEntitys;
                smConfigEntitys.forEach(o ->{
                    SuConfigEntity suConfigEntity = suConfigEntities.stream().filter(i ->{
                        return i.getName().equals(o.getName());
                    }).findFirst().get();
                    if(suConfigEntity != null){
                        o.setVal(suConfigEntity.getVal());
                    }
                });
                return smConfigEntitys;
            }
            case ConfigTypeConstant.PARENT_CONFIG:{
                SuUserEntity parent = userFeignClient.superUser(userId, UserTypeConstant.PARENT).getData();
                if(parent == null){
                    return smConfigEntitys;
                }else {
                    List<SuConfigEntity> suConfigEntities = userFeignClient.getAllConfig(parent.getId()).getData();
                    if(suConfigEntities == null)
                        return smConfigEntitys;
                    smConfigEntitys = unionConfig(smConfigEntitys,suConfigEntities);
                    return smConfigEntitys;
                }
            }
            case ConfigTypeConstant.PROXY_CONFIG:{
                SuUserEntity proxy = userFeignClient.superUser(userId, UserTypeConstant.PROXY).getData();
                if(proxy == null){
                    return smConfigEntitys;
                }else {
                    List<SuConfigEntity> suConfigEntities = userFeignClient.getAllConfig(proxy.getId()).getData();
                    if(suConfigEntities == null)
                        return smConfigEntitys;
                    smConfigEntitys = unionConfig(smConfigEntitys,suConfigEntities);
                    return smConfigEntitys;
                }
            }
        }
        throw new GlobleException(MsgEnum.TYPE_NOTFOUND_ERROR);
    }

    /**
     * 合并配置
     * @param smConfigEntities  :系统配置
     * @param suConfigEntities  :目标配置
     * @return
     */
    private List<SmConfigEntity> unionConfig(List<SmConfigEntity> smConfigEntities,List<SuConfigEntity> suConfigEntities){
        smConfigEntities.forEach(o ->{
            SuConfigEntity suConfigEntity = suConfigEntities.stream().filter(i ->{
                return i.getName().equals(o.getName());
            }).findFirst().get();
            if(suConfigEntity != null){
                o.setVal(suConfigEntity.getVal());
            }
        });
        return smConfigEntities;
    }
}
