package com.xm.api_user.service;

import com.xm.comment_serialize.module.user.bo.UserProfitBo;
import com.xm.comment_serialize.module.user.dto.ProxyProfitDto;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.ex.RolePermissionEx;
import com.xm.comment_serialize.module.user.ex.UserRoleEx;
import com.xm.comment_serialize.module.user.form.GetUserInfoForm;
import com.xm.comment_serialize.module.user.form.UpdateUserInfoForm;
import com.xm.comment_serialize.module.user.vo.ProxyInfoVo;
import com.xm.comment_serialize.module.user.vo.UserProfitVo;
import com.xm.comment_utils.mybatis.PageBean;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;

public interface UserService {
    /**
     * 获取单个用户信息
     * @param getUserInfoForm
     * @return
     * @throws WxErrorException
     */
    public SuUserEntity getUserInfo(GetUserInfoForm getUserInfoForm) throws WxErrorException;

    /**
     * 添加一个用户
     * @param openId
     * @param shareUserId
     * @return
     */
    public SuUserEntity addNewUser(String openId,Integer shareUserId);

    /**
     * 更新一个用户资料
     * @param userId
     * @param updateUserInfoForm
     */
    public void updateUserInfo(Integer userId, UpdateUserInfoForm updateUserInfoForm);

    /**
     * 获取用户权限
     * @param userId
     * @return
     */
    public List<RolePermissionEx> getUserRole(Integer userId);

    /**
     * 获取上级用户
     * @param userId
     * @param userType  :UserTypeContstant
     * @return
     */
    public SuUserEntity getSuperUser(Integer userId,int userType);

    /**
     * 获取用户代理收益信息
     * @param userId
     * @param state
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageBean<ProxyProfitDto> getProxyProfit(Integer userId, Integer state, Integer orderColumn, Integer orderBy, Integer pageNum, Integer pageSize);

    /**
     * 获取用户代理信息
     * @param userId
     * @return
     */
    public ProxyInfoVo getProxyInfo(Integer userId);

    /**
     * 获取用户收益概略信息
     * @param userId
     * @return
     */
    public UserProfitBo getUserProftList(Integer userId);

    public UserProfitBo getUserProftDesc(Integer userId);
}
