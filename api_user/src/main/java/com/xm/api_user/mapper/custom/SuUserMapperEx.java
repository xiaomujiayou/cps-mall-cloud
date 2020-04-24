package com.xm.api_user.mapper.custom;

import com.xm.api_user.utils.MyMapper;
import com.xm.comment_serialize.module.user.dto.ProxyProfitDto;
import com.xm.comment_serialize.module.user.entity.SuRoleEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.ex.RolePermissionEx;

import java.util.List;

public interface SuUserMapperEx extends MyMapper<SuUserEntity> {

    /**
     * 获取代理收益详情
     * @param userId
     * @return
     */
    List<ProxyProfitDto> getProxyProfit(Integer userId,Integer billState,String orderBy,Integer start,Integer size);

    Integer getIndirectUserCount(Integer userId);
}
