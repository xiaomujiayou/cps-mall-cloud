package com.xm.api_user.service;

import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_utils.mybatis.PageBean;

import java.util.List;

public interface ProductService {

    /**
     * 获取用户商品信息
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param suProductType
     * @return
     */
    public PageBean<SmProductEntity> getUserProduct(Integer userId, Integer pageNum, Integer pageSize, Integer suProductType);

    /**
     * 添加浏览历史记录
     * @param userId
     * @param platformType
     * @param goodsId
     */
    public void addHistory(Integer userId,Integer platformType,String goodsId,Integer shareUserId);

    /**
     * @param userId
     * @param id
     * @param isAll
     */
    public void delHistory(Integer userId,Integer id,Boolean isAll);

    /**
     * 是否收藏
     * @param userId
     * @param platformType
     * @param goodsId
     * @return
     */
    public boolean isCollect(Integer userId,Integer platformType,String goodsId);

    /**
     * 取消或搜藏一个商品
     * @param userId
     * @param platformType
     * @param goodsId
     */
    public void collect(Integer userId,Integer platformType,String goodsId,Integer shareUserId,Boolean isCollect);
}
