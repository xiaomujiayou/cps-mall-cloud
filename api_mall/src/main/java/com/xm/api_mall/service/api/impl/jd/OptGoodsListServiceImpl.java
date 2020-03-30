package com.xm.api_mall.service.api.impl.jd;

import com.xm.api_mall.service.api.OptGoodsListService;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.GoodsListForm;
import com.xm.comment_serialize.module.mall.form.ThemeGoodsListForm;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.stereotype.Service;

@Service("jdOptGoodsListService")
public class OptGoodsListServiceImpl implements OptGoodsListService {


    @Override
    public PageBean<SmProductEntityEx> theme(ThemeGoodsListForm themeGoodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> one(GoodsListForm goodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> two(GoodsListForm goodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> three(GoodsListForm goodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> four(GoodsListForm goodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> five(GoodsListForm goodsListForm) throws Exception {
        return null;
    }
}
