package com.xm.comment_serialize.module.user.dto;

import com.xm.comment_serialize.module.user.entity.SuRoleEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.ex.UserRoleEx;
import lombok.Data;

import java.util.List;

@Data
public class UserAuthDto extends SuUserEntity {
    private List<UserRoleEx> userRoleExes;
}
