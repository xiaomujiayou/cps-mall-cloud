package com.xm.comment_feign.module.user.feign.fallback;

import com.xm.comment_feign.module.user.feign.UserFeignClient;
import com.xm.comment_serialize.module.lottery.ex.SlPropSpecEx;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import com.xm.comment_serialize.module.user.vo.MenuTipVo;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_serialize.module.user.entity.SuConfigEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.ex.RolePermissionEx;
import com.xm.comment_serialize.module.user.form.AddSearchForm;
import com.xm.comment_serialize.module.user.form.GetUserInfoForm;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserFeignClientFallBack implements UserFeignClient {

    @Override
    public SuUserEntity getUserInfo(GetUserInfoForm getUserInfoForm) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public SuUserEntity getInfoDetail(Integer userId) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public List<RolePermissionEx> role(Integer userId) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public List<SuConfigEntity> getAllConfig(Integer userId) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public SuConfigEntity getOneConfig(Integer userId, String key) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public SuUserEntity superUser(Integer userId, int userType) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public void addProductHistory(Integer userId, Integer platformType, String goodsId) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public void addSearch(Integer userId, AddSearchForm addSearchForm) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public SuBillToPayBo createByProp(SlPropSpecEx slPropSpecEx) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public void addNum(Integer userId, List<Integer> menuIds) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public void addRedPoint(Integer userId, List<Integer> menuIds) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public void del(Integer userId, List<Integer> menuIds) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public List<MenuTipVo> get(Integer userId, List<Integer> menuIds) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }
}
