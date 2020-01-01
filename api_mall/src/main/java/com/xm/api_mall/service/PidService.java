package com.xm.api_mall.service;

import com.xm.comment_serialize.module.mall.entity.SmPidEntity;

public interface PidService {

    public SmPidEntity generatePid();

    public  SmPidEntity getPid(Integer id);
}
