package com.xm.api_user.service;

import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_serialize.module.user.vo.ShareVo;
import com.xm.comment_utils.mybatis.PageBean;

/**
 * 用户分享服务
 */
public interface ShareService {

    /**
     * 分享一个商品
     * @param userId
     * @param goodsId
     * @param platformType
     */
    public void shareOne(Integer userId,String goodsId,Integer platformType);


    /**
     * 分享的商品被浏览
     * @param userId
     * @param goodsId
     * @param platformType
     */
    public void show(Integer userId,String goodsId,Integer platformType);

    /**
     * 分享的商品被购买
     */
    public void buy(SuOrderEntity suOrderEntity);

    /**
     * 商品购买失败
     * @param suOrderEntity
     */
    public void buyFail(SuOrderEntity suOrderEntity);


    /**
     * 获取分享列表
     * @param userId
     * @param orderType
     * @param order
     * @param pageNum
     * @param pageSize
     */
    public PageBean<ShareVo> getList(Integer userId, Integer orderType, Integer order, Integer pageNum, Integer pageSize);

    /**
     * 删除分享商品
     * @param userId
     * @param shareId
     */
    public void del(Integer userId,Integer shareId);
}
