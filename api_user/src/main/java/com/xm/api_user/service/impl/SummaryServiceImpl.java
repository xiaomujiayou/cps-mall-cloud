package com.xm.api_user.service.impl;

import com.xm.api_user.mapper.SuSummaryMapper;
import com.xm.api_user.service.SummaryService;
import com.xm.comment_serialize.module.user.entity.SuSummaryEntity;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;

@Service("summaryService")
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    private RedisLockRegistry redisLockRegistry;
    @Autowired
    private SuSummaryMapper suSummaryMapper;

    @Override
    public SuSummaryEntity getUserSummary(Integer userId) {
        SuSummaryEntity record = new SuSummaryEntity();
        record.setUserId(userId);
        return suSummaryMapper.selectOne(record);
    }

    @Override
    public void updateSummary(SuSummaryEntity suSummaryEntity) {
        suSummaryEntity.setUpdateTime(new Date());
        suSummaryMapper.updateByPrimaryKeySelective(suSummaryEntity);
    }

    @Override
    public SuSummaryEntity createNewSummary(Integer userId) {
        SuSummaryEntity suSummaryEntity = new SuSummaryEntity();
        suSummaryEntity.setUserId(userId);
        suSummaryEntity.setUpdateTime(new Date());
        suSummaryEntity.setCreateTime(suSummaryEntity.getUpdateTime());
        suSummaryMapper.insertUseGeneratedKeys(suSummaryEntity);
        return suSummaryEntity;
    }
}
