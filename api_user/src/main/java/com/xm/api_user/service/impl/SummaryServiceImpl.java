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
        Lock lock = redisLockRegistry.obtain("lock");
        lock.lock();
        try {
            SuSummaryEntity record = new SuSummaryEntity();
            record.setUserId(userId);
            List<SuSummaryEntity> target = suSummaryMapper.select(record);
            if(target == null || target.size() <= 0) {
                SuSummaryEntity suSummaryEntity = createNewSummary(userId);
                return suSummaryEntity;
            }
            return target.get(0);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void updateSummary(SuSummaryEntity suSummaryEntity) {
        suSummaryEntity.setUpdateTime(new Date());
        suSummaryMapper.updateByPrimaryKeySelective(suSummaryEntity);
    }

    private SuSummaryEntity createNewSummary(Integer userId) {
        SuSummaryEntity suSummaryEntity = new SuSummaryEntity();
        suSummaryEntity.setUserId(userId);
        suSummaryEntity.setUpdateTime(new Date());
        suSummaryEntity.setCreateTime(suSummaryEntity.getUpdateTime());
        suSummaryMapper.insertUseGeneratedKeys(suSummaryEntity);
        return suSummaryEntity;
    }


}
