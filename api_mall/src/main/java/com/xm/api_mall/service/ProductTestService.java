package com.xm.api_mall.service;

import com.xm.comment_serialize.module.mall.bo.ShareLinkBo;
import com.xm.comment_serialize.module.mall.form.GetProductSaleInfoForm;

/**
 * 商品测试接口
 * 上线前应移除
 */
public interface ProductTestService {

    /**
     * 模拟购买
     * @param userId
     * @param pid
     * @param productSaleInfoForm
     * @return
     * @throws Exception
     */
    public ShareLinkBo saleInfo(Integer userId, String pid, GetProductSaleInfoForm productSaleInfoForm) throws Exception;
}
