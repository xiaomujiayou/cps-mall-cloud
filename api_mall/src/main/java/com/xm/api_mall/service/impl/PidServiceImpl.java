package com.xm.api_mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.xm.api_mall.mapper.SmPidMapper;
import com.xm.api_mall.service.PidService;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_serialize.module.mall.entity.SmPidEntity;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("pidService")
public class PidServiceImpl implements PidService {

    @Autowired
    private SmPidMapper smPidMapper;

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public SmPidEntity generatePid() {
        SmPidEntity record = new SmPidEntity();
        record.setState(1);
        PageHelper.startPage(1,1);
        SmPidEntity smPidEntity = smPidMapper.selectOne(record);
        if(smPidEntity == null)
            throw new GlobleException(MsgEnum.NO_DATA_ERROR,"pid 获取失败");
        record.setState(2);
        record.setId(smPidEntity.getId());
        smPidMapper.updateByPrimaryKeySelective(record);
        return smPidEntity;
    }

    public  SmPidEntity getPid(Integer id){
        return smPidMapper.selectByPrimaryKey(id);
    }
}
