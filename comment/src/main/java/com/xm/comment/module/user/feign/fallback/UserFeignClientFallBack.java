package com.xm.comment.module.user.feign.fallback;

import com.xm.comment.module.user.feign.UserFeignClient;
import com.xm.comment.response.Msg;
import com.xm.comment.response.MsgEnum;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.user.entity.SuConfigEntity;
import com.xm.comment_serialize.module.user.entity.SuPidEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.ex.RolePermissionEx;
import com.xm.comment_serialize.module.user.form.AddSearchForm;
import com.xm.comment_serialize.module.user.form.GetUserInfoForm;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserFeignClientFallBack implements UserFeignClient {

    @Override
    public Msg<SuUserEntity> getUserInfo(GetUserInfoForm getUserInfoForm) {
        return R.error(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public Msg<List<RolePermissionEx>> role(Integer userId) {
        return R.error(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public Msg<List<SuConfigEntity>> getAllConfig(Integer userId) {
        return R.error(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public Msg<SuConfigEntity> getOneConfig(Integer userId, String key) {
        return R.error(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public Msg<SuUserEntity> superUser(Integer userId, int userType) {
        return R.error(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public Msg addProductHistory(Integer userId, Integer platformType, String goodsId) {
        return R.error(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public Msg<SuPidEntity> getPid(Integer userId, Integer platformType) {
        return R.error(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public Msg addSearch(Integer userId, AddSearchForm addSearchForm) {
        return R.error(MsgEnum.SERVICE_AVAILABLE);
    }

}
