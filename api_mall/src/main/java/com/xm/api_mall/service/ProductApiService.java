package com.xm.api_mall.service;

import com.xm.comment_serialize.module.mall.bo.ProductCriteriaBo;
import com.xm.comment_serialize.module.mall.bo.ShareLinkBo;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_utils.mybatis.PageBean;

import java.util.List;

public interface ProductApiService {

    /**
     * 查询商品接口
     * @param criteria
     * @return
     */
    public PageBean<SmProductEntity> getProductByCriteria(ProductCriteriaBo criteria) throws Exception;

    /**
     * 获取商品详情
     * @param goodsIds
     * @return
     */
    public List<SmProductEntity> details(List<Long> goodsIds) throws Exception;

    /**
     * 获取单个商品详情
     * 内容更加丰富
     * @param goodsId
     * @return
     * @throws Exception
     */
    public SmProductEntity detail(Long goodsId) throws Exception;

    /**
     * 获取商品购买信息
     * @param customParams
     * @param pId
     * @param goodsId
     * @return
     * @throws Exception
     */
    public ShareLinkBo getShareLink(String customParams,String pId, Long goodsId) throws Exception;

    /**
     * 生成推广位id
     */
    public String generatePid(Integer userId) throws Exception;

}
