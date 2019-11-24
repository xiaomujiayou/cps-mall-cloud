package com.xm.comment_serialize.module.user.ex;

import com.xm.comment_serialize.module.user.entity.SuPermissionEntity;
import com.xm.comment_serialize.module.user.entity.SuRoleEntity;
import lombok.Data;

import java.util.List;

@Data
public class RolePermissionEx extends SuRoleEntity {
    private List<SuPermissionEntity> suPermissionEntities;
}
