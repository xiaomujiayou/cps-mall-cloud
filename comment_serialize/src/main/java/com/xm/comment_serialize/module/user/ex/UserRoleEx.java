package com.xm.comment_serialize.module.user.ex;

import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import lombok.Data;

import java.util.List;

@Data
public class UserRoleEx extends SuUserEntity {
    private List<RolePermissionEx> roleExes;
}
