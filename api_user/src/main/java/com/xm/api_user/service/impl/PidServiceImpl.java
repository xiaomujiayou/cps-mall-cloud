package com.xm.api_user.service.impl;

import com.xm.api_user.mapper.SuPidMapper;
import com.xm.api_user.service.PidService;
import com.xm.comment.module.mall.feign.MallFeignClient;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.user.entity.SuPidEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service("pidService")
public class PidServiceImpl implements PidService {

    @Autowired
    private SuPidMapper suPidMapper;
    @Autowired
    private MallFeignClient mallFeignClient;

    @Override
    public SuPidEntity getPid(Integer userId, Integer platformType) {
        SuPidEntity suPidEntity = new SuPidEntity();
        suPidEntity.setUserId(userId);
        suPidEntity.setPlatformType(platformType);
        suPidEntity = suPidMapper.selectOne(suPidEntity);
        if(suPidEntity != null && suPidEntity.getId() != null){
            if(suPidEntity.getPId() != null)
                return suPidEntity;
            //删除重新生成
            suPidMapper.deleteByPrimaryKey(suPidEntity.getId());
        }
        suPidEntity = generatePid(userId,platformType);
        return suPidEntity;
    }

    private SuPidEntity generatePid(Integer userId, Integer platformType){
        String pid = mallFeignClient.generatePid(userId,platformType).getData();
        SuPidEntity suPidEntity = new SuPidEntity();
        suPidEntity.setPlatformType(platformType);
        suPidEntity.setUserId(userId);
        suPidEntity.setPId(pid);
        suPidEntity.setCreateTime(new Date(System.currentTimeMillis()));
        suPidMapper.insertSelective(suPidEntity);
        return suPidEntity;
    }
}
