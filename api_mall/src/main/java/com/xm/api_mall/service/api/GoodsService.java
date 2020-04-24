package com.xm.api_mall.service.api;

import com.xm.comment_serialize.module.mall.form.*;
import com.xm.comment_serialize.module.mall.bo.ShareLinkBo;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.vo.SmProductSimpleVo;

import java.util.List;

public interface GoodsService {

    /**
     * 获取商品详情
     * @param goodsDetailForm
     * @return
     * @throws Exception
     */
    public SmProductEntityEx detail(GoodsDetailForm goodsDetailForm) throws Exception;

    /**
     * 批量获取商品详情
     * @param goodsDetailsForm
     * @return
     * @throws Exception
     */
    public List<SmProductEntity> details(GoodsDetailsForm goodsDetailsForm) throws Exception;

    /**
     * 获取商品简略信息
     * @param baseGoodsDetailForm
     * @return
     * @throws Exception
     */
    public SmProductSimpleVo basicDetail(BaseGoodsDetailForm baseGoodsDetailForm) throws Exception;

    /**
     * 获取购买信息
     * @return
     */
    public ShareLinkBo saleInfo(SaleInfoForm saleInfoForm) throws Exception;

}
