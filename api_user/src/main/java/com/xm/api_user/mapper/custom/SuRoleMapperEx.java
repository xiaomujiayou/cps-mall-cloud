package com.xm.api_user.mapper.custom;

import com.xm.api_user.utils.MyMapper;
import com.xm.comment_serialize.module.user.entity.SuRoleEntity;
import com.xm.comment_serialize.module.user.ex.RolePermissionEx;

import java.util.List;

public interface SuRoleMapperEx extends MyMapper<SuRoleEntity> {

    /**
     * 获取用户角色权限
     * @param userId
     * @return
     */
    List<RolePermissionEx> getUserRoleEx(Integer userId);

}
