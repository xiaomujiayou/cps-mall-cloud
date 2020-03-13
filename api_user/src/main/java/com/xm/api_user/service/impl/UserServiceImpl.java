package com.xm.api_user.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.xm.api_user.mapper.*;
import com.xm.api_user.mapper.custom.SuOrderMapperEx;
import com.xm.api_user.mapper.custom.SuRoleMapperEx;
import com.xm.api_user.mapper.custom.SuUserMapperEx;
import com.xm.api_user.service.UserService;
import com.xm.comment_mq.message.config.UserActionConfig;
import com.xm.comment_mq.message.impl.UserAddProxyMessage;
import com.xm.comment_mq.message.impl.UserFristLoginMessage;
import com.xm.comment_serialize.module.user.bo.UserProfitBo;
import com.xm.comment_serialize.module.user.constant.BillStateConstant;
import com.xm.comment_serialize.module.user.constant.BillTypeConstant;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_feign.module.mall.feign.MallFeignClient;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_serialize.module.mall.constant.ConfigEnmu;
import com.xm.comment_serialize.module.mall.constant.ConfigTypeConstant;
import com.xm.comment_serialize.module.mall.entity.SmPidEntity;
import com.xm.comment_serialize.module.user.constant.UserTypeConstant;
import com.xm.comment_serialize.module.user.dto.ProxyProfitDto;
import com.xm.comment_serialize.module.user.entity.*;
import com.xm.comment_serialize.module.user.ex.RolePermissionEx;
import com.xm.comment_serialize.module.user.form.GetUserInfoForm;
import com.xm.comment_serialize.module.user.form.UpdateUserInfoForm;
import com.xm.comment_serialize.module.user.vo.ProxyInfoVo;
import com.xm.comment_serialize.module.user.vo.UserProfitVo;
import com.xm.comment_utils.encry.MD5;
import com.xm.comment_utils.mybatis.PageBean;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Lazy
    @Autowired
    private UserService userService;
    @Autowired
    private SuUserMapper suUserMapper;
    @Autowired
    private SuUserMapperEx suUserMapperEx;
    @Autowired
    private SuRoleMapperEx suRoleMapperEx;
    @Autowired
    private SuPermissionMapper suPermissionMapper;
    @Autowired
    private SuUserRoleMapMapper suUserRoleMapMapper;
    @Autowired
    private SuRolePermissionMapMapper suRolePermissionMapMapper;
    @Autowired
    private WxMaService wxMaService;
    @Autowired
    private MallFeignClient mallFeignClient;
    @Autowired
    private SuOrderMapper suOrderMapper;
    @Autowired
    private SuOrderMapperEx suOrderMapperEx;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public SuUserEntity getUserInfo(GetUserInfoForm getUserInfoForm) throws WxErrorException {
        if(StringUtils.isNotBlank(getUserInfoForm.getCode())){
            WxMaJscode2SessionResult wxMaJscode2SessionResult = wxMaService.getUserService().getSessionInfo(getUserInfoForm.getCode());
            SuUserEntity record = new SuUserEntity();
            record.setOpenId(wxMaJscode2SessionResult.getOpenid());
            record = suUserMapper.selectOne(record);
            if(record != null)
                return record;
            return userService.addNewUser(wxMaJscode2SessionResult.getOpenid(),getUserInfoForm.getShareUserId());
        }else if(StringUtils.isNotBlank(getUserInfoForm.getOpenId())){
            SuUserEntity record = new SuUserEntity();
            record.setOpenId(getUserInfoForm.getOpenId());
            return suUserMapper.selectOne(record);
        }else {
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR);
        }
    }

    /**
     * 添加一个新用户
     * @param openId
     * @return
     */
    public SuUserEntity addNewUser(String openId,Integer shareUserId){
        SmPidEntity smPidEntity = mallFeignClient.generatePid();
        SuUserEntity user = new SuUserEntity();
        if(shareUserId != null)
            user.setParentId(shareUserId);
        user.setOpenId(openId);
        user.setUserSn(MD5.md5(openId,""));
        user.setNickname("Su_"+ user.getUserSn().substring(0,5));
        user.setSex(0);
        user.setCreateTime(new Date());
        user.setLastLogin(new Date());
        user.setPid(smPidEntity.getId());
        suUserMapper.insertSelective(user);
        SuUserEntity record = new SuUserEntity();
        record.setOpenId(openId);
        record = suUserMapper.selectOne(record);
        return record;
    }


    @Override
    public void updateUserInfo(Integer userId, UpdateUserInfoForm updateUserInfoForm){
        SuUserEntity suUserEntity = new SuUserEntity();
        suUserEntity.setId(userId);
        suUserEntity.setNickname(updateUserInfoForm.getNickName());
        suUserEntity.setHeadImg(updateUserInfoForm.getAvatarUrl());
        suUserEntity.setSex(updateUserInfoForm.getGender());
        suUserEntity.setLanguage(updateUserInfoForm.getLanguage());
        suUserEntity.setCity(updateUserInfoForm.getCity());
        suUserEntity.setProvince(updateUserInfoForm.getProvince());
        suUserEntity.setCountry(updateUserInfoForm.getCountry());
        suUserMapper.updateByPrimaryKeySelective(suUserEntity);
    }

    @Override
    public List<RolePermissionEx> getUserRole(Integer userId) {
        return suRoleMapperEx.getUserRoleEx(userId);
    }

    @Override
    public SuUserEntity getSuperUser(Integer userId, int userType) {
        SuUserEntity self = suUserMapper.selectByPrimaryKey(userId);
        if(self == null){
            throw new GlobleException(MsgEnum.USER_NOFOUND_ERROR,"id:"+userId);
        }
        switch (userType){
            case UserTypeConstant.SELF:{
                return self;
            }
            case UserTypeConstant.PARENT:{
                if(self.getParentId() == null){
                    throw new NullPointerException(String.format("用户：%s 不存在父用户",userId));
                }
                SuUserEntity parent = suUserMapper.selectByPrimaryKey(self.getParentId());
                if(parent == null){
                    throw new GlobleException(MsgEnum.USER_ID_ERROR);
                }
                return parent;
            }
            case UserTypeConstant.PROXY:{
                if(self.getParentId() == null || self.getParentId() == 0){
                    return null;
                }
                Integer level = Integer.valueOf(mallFeignClient.getOneConfig(userId, ConfigEnmu.PROXY_LEVEL.getName(), ConfigTypeConstant.SYS_CONFIG).getVal());
                SuUserEntity target = null;
                for (int i = 0; i < level; i++) {
                    target = suUserMapper.selectByPrimaryKey(self.getParentId());
                    if(target.getParentId() == null || target.getParentId() == 0){
                        break;
                    }
                }
                return target;
            }
        }
        throw new GlobleException(MsgEnum.TYPE_NOTFOUND_ERROR);
    }

    @Override
    public PageBean<ProxyProfitDto> getProxyProfit(Integer userId, Integer state, Integer orderColumn, Integer orderBy, Integer pageNum, Integer pageSize) {
        if(orderBy != null && orderColumn != null && orderBy != 0){
            String order = orderBy == 1?"asc":"desc";
            switch (orderColumn){
                case 1:{
                    OrderByHelper.orderBy("nickname " + order);
                    break;
                }
                case 2:{
                    OrderByHelper.orderBy("proxy_profit " + order);
                    break;
                }
                case 3:{
                    OrderByHelper.orderBy("proxy_num " + order);
                    break;
                }
                case 4:{
                    OrderByHelper.orderBy("create_time " + order);
                    break;
                }
            }

        }
        PageHelper.startPage(pageNum,pageSize);
        List<ProxyProfitDto> proxyProfitDtos = suUserMapperEx.getProxyProfit(userId,state);
        return new PageBean<>(proxyProfitDtos);
    }

    @Override
    public ProxyInfoVo getProxyInfo(Integer userId) {
        ProxyInfoVo proxyInfoVo = new ProxyInfoVo();
        proxyInfoVo.setTotalIndirectProxy(suUserMapperEx.getIndirectUserCount(userId));
        SuUserEntity example = new SuUserEntity();
        example.setParentId(userId);
        proxyInfoVo.setTotalDirectProxy(suUserMapper.selectCount(example));
        return proxyInfoVo;
    }

    @Override
    public UserProfitBo getUserProftList(Integer userId) {
        UserProfitBo userProfitBo = new UserProfitBo();
        Map<String, BigDecimal> orderInfo = suOrderMapperEx.getUserOrderAbout(userId);
        userProfitBo.setTotalCoupon(orderInfo.get("totalCoupon").intValue());
        userProfitBo.setTotalConsumption(orderInfo.get("totalConsumption").intValue());
        userProfitBo.setTodayProfit(suOrderMapperEx.getUserTotalCommission(userId,null, DateUtil.parse(DateUtil.today()),new Date()).intValue());
        userProfitBo.setTotalProfit(suOrderMapperEx.getUserTotalCommission(userId,null,null,null).intValue());
        userProfitBo.setWaitProfit(suOrderMapperEx.getUserTotalCommission(userId, CollUtil.newArrayList(BillStateConstant.WAIT,BillStateConstant.READY),null,null).intValue());
        userProfitBo.setTotalShare(suOrderMapperEx.getUserShareOrderAbout(userId).intValue());
        ProxyInfoVo proxyInfoVo = getProxyInfo(userId);
        userProfitBo.setTotalProxyUser(proxyInfoVo.getTotalDirectProxy()+proxyInfoVo.getTotalIndirectProxy());
        return userProfitBo;
    }

    @Override
    public UserProfitBo getUserProftDesc(Integer userId) {
        UserProfitBo userProfitBo = new UserProfitBo();
        Map<String, BigDecimal> orderInfo = suOrderMapperEx.getUserOrderAbout(userId);
        userProfitBo.setTotalCoupon(orderInfo.get("totalCoupon").intValue());
        userProfitBo.setTotalConsumption(orderInfo.get("totalConsumption").intValue());
        userProfitBo.setTotalCommission(suOrderMapperEx.getUserTotalCommission(userId,CollUtil.newArrayList(3),null,null).intValue());
        return userProfitBo;
    }
}
