package com.xm.api_mall.service.api.impl.def;

import com.xm.api_mall.service.api.GoodsListService;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.GoodsListForm;
import com.xm.comment_serialize.module.mall.form.KeywordGoodsListForm;
import com.xm.comment_serialize.module.mall.form.OptionGoodsListForm;
import com.xm.comment_serialize.module.mall.form.SimilarGoodsListForm;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.stereotype.Service;

@Service("goodsListService")
public class GoodsListServiceImpl implements GoodsListService {
    @Override
    public PageBean<SmProductEntityEx> index(GoodsListForm goodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> recommend(GoodsListForm goodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> my(GoodsListForm goodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> keyword(KeywordGoodsListForm keywordGoodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> option(OptionGoodsListForm optionGoodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> similar(SimilarGoodsListForm similarGoodsListForm) throws Exception {
        return null;
    }
}
