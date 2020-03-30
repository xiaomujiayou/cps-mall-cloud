package com.xm.api_mall.service.api.impl.jd;

import com.xm.api_mall.service.api.GoodsService;
import com.xm.comment_serialize.module.mall.bo.ShareLinkBo;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.BaseGoodsDetailForm;
import com.xm.comment_serialize.module.mall.form.GoodsDetailForm;
import com.xm.comment_serialize.module.mall.form.GoodsDetailsForm;
import com.xm.comment_serialize.module.mall.form.SaleInfoForm;
import com.xm.comment_serialize.module.mall.vo.SmProductSimpleVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("jdGoodsService")
public class GoodsServiceImpl implements GoodsService {

    @Override
    public SmProductEntityEx detail(GoodsDetailForm goodsDetailForm) throws Exception {
        return null;
    }

    @Override
    public List<SmProductEntity> details(GoodsDetailsForm goodsDetailsForm) throws Exception {
        return null;
    }

    @Override
    public SmProductSimpleVo basicDetail(BaseGoodsDetailForm baseGoodsDetailForm) throws Exception {
        return null;
    }

    @Override
    public ShareLinkBo saleInfo(SaleInfoForm saleInfoForm) throws Exception {
        return null;
    }
}
