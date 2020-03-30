package com.xm.api_user.service.impl;

import com.github.pagehelper.PageHelper;
import com.xm.api_user.mapper.SuPidMapper;
import com.xm.api_user.service.PidService;
import com.xm.comment_serialize.module.user.entity.SuPidEntity;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("pidService")
public class PidServiceImpl implements PidService {

    @Autowired
    private SuPidMapper suPidMapper;

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public SuPidEntity generatePid() {
        SuPidEntity record = new SuPidEntity();
        record.setState(1);
        PageHelper.startPage(1,1);
        SuPidEntity suPidEntity = suPidMapper.selectOne(record);
        if(suPidEntity == null)
            throw new GlobleException(MsgEnum.NO_DATA_ERROR,"pid 获取失败");
        record.setState(2);
        record.setId(suPidEntity.getId());
        suPidMapper.updateByPrimaryKeySelective(record);
        return suPidEntity;
    }

    public SuPidEntity getPid(Integer id){
        return suPidMapper.selectByPrimaryKey(id);
    }
}
