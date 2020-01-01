package com.xm.api_mall.service;

import com.xm.comment_serialize.module.mall.bo.ShareLinkBo;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.ProductDetailForm;
import com.xm.comment_serialize.module.mall.form.ProductListForm;
import com.xm.comment_utils.mybatis.PageBean;

import java.util.List;

public interface ProductService {

    /**
     * 通过option获取商品
     * @return
     */
    public PageBean<SmProductEntity> optionList(Integer userId, ProductListForm productListForm) throws Exception;

    /**
     * 获取类似商品
     * @return
     */
    public PageBean<SmProductEntity> similarList(Integer userId,ProductListForm productListForm);

    /**
     * 获取推荐商品
     * @return
     */
    public PageBean<SmProductEntity> bestList (Integer userId,ProductListForm productListForm) throws Exception;

    /**
     * 根据关键字获取商品
     * @return
     */
    public PageBean<SmProductEntity> keywordList(Integer userId,ProductListForm productListForm) throws Exception;

    /**
     * 获取热销商品
     * @return
     */
    public PageBean<SmProductEntity> hotList(Integer userId,ProductListForm productListForm) throws Exception;

    /**
     * 获取自定义商品
     * @return
     */
    public PageBean<SmProductEntity> customList(Integer userId,ProductListForm productListForm) throws Exception;

    /**
     * 获取猜你喜欢商品
     * @return
     */
    public PageBean<SmProductEntity> likeList(Integer userId,ProductListForm productListForm) throws Exception;

    /**
     * 主题商品列表
     * @param userId
     * @param productListForm
     * @return
     * @throws Exception
     */
    public PageBean<SmProductEntity> themeList(Integer userId,ProductListForm productListForm) throws Exception;

    /**
     * 获取营销活动列表
     * @param userId
     * @param productListForm
     * @return
     * @throws Exception
     */
    public PageBean<SmProductEntity> recommendList(Integer userId,ProductListForm productListForm) throws Exception;

    /**
     * 获取主题活动
     * @return
     * @throws Exception
     */
    public List<SmBannerEntity> themes() throws Exception;

    /**
     * 批量获取商品详情
     * @param goodsIds
     * @return
     * @throws Exception
     */
    public List<SmProductEntity> details(List<String> goodsIds) throws Exception;

    /**
     * 获取商品详情
     * @param goodsId
     * @return
     * @throws Exception
     */
    public SmProductEntityEx detail(String goodsId,Integer userId, Integer shareUserId) throws Exception;

    /**
     * 获取购买信息
     * @param userId
     * @param appType   :所属平台类型(AppTypeConstant)
     * @param fromUser  :分享的用户
     * @param goodsId
     * @return
     */
    public ShareLinkBo saleInfo(Integer userId,String pid,Integer appType,Integer fromUser,String goodsId) throws Exception;


}
