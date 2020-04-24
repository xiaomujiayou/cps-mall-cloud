package com.xm.api_user.service.impl;

import com.xm.api_user.mapper.SuSummaryMapper;
import com.xm.api_user.service.SummaryService;
import com.xm.comment.utils.LockHelper;
import com.xm.comment_serialize.module.user.entity.SuSummaryEntity;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;

@Slf4j
@Service("summaryService")
public class SummaryServiceImpl implements SummaryService {

    @Lazy
    @Autowired
    private SummaryService summaryService;
    @Autowired
    private SuSummaryMapper suSummaryMapper;

    @Override
    public SuSummaryEntity getUserSummary(Integer userId) {
        SuSummaryEntity record = new SuSummaryEntity();
        record.setUserId(userId);
        record = suSummaryMapper.selectOne(record);
        if(record == null || record.getId() == null)
            record = summaryService.createNewSummary(userId);
        return record;
    }

    @Override
    public void updateSummary(SuSummaryEntity suSummaryEntity) {
        suSummaryEntity.setUpdateTime(new Date());
        suSummaryMapper.updateByPrimaryKeySelective(suSummaryEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SuSummaryEntity createNewSummary(Integer userId) {
        SuSummaryEntity suSummaryEntity = new SuSummaryEntity();
        suSummaryEntity.setUserId(userId);
        suSummaryEntity.setUpdateTime(new Date());
        suSummaryEntity.setCreateTime(suSummaryEntity.getUpdateTime());
        try {
            suSummaryMapper.insertUseGeneratedKeys(suSummaryEntity);
        }catch (DuplicateKeyException e){
            log.warn("SuSummaryEntity 数据插入重复 userId：{}",userId);
            SuSummaryEntity record = new SuSummaryEntity();
            record.setUserId(userId);
            return suSummaryMapper.selectOne(record);
        }
        return suSummaryEntity;
    }
}
