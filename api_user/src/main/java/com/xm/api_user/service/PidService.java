package com.xm.api_user.service;

import com.xm.comment_serialize.module.user.entity.SuPidEntity;

public interface PidService {

    public SuPidEntity generatePid();

    public SuPidEntity getPid(Integer id);
}
