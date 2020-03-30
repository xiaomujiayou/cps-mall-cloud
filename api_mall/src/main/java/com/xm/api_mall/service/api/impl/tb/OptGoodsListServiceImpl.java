package com.xm.api_mall.service.api.impl.tb;

import cn.hutool.core.collection.CollUtil;
import com.xm.api_mall.component.PddSdkComponent;
import com.xm.api_mall.component.TbSdkComponent;
import com.xm.api_mall.service.api.OptGoodsListService;
import com.xm.comment_api.config.TbApiConfig;
import com.xm.comment_serialize.module.mall.bo.ProductCriteriaBo;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.GoodsListForm;
import com.xm.comment_serialize.module.mall.form.ThemeGoodsListForm;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tbOptGoodsListService")
public class OptGoodsListServiceImpl implements OptGoodsListService {

    @Autowired
    private TbApiConfig tbApiConfig;
    @Autowired
    private TbSdkComponent tbSdkComponent;


    @Override
    public PageBean<SmProductEntityEx> theme(ThemeGoodsListForm themeGoodsListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> one(GoodsListForm goodsListForm) throws Exception {
        return tbSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                tbSdkComponent.optSearch(
                        goodsListForm,
                        tbApiConfig.getAdzoneId(),
                        4093L,
                        null));
    }

    @Override
    public PageBean<SmProductEntityEx> two(GoodsListForm goodsListForm) throws Exception {
        return tbSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                tbSdkComponent.optSearch(
                        goodsListForm,
                        tbApiConfig.getAdzoneId(),
                        4094L,
                        null));
    }

    @Override
    public PageBean<SmProductEntityEx> three(GoodsListForm goodsListForm) throws Exception {
        return tbSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                tbSdkComponent.optSearch(
                        goodsListForm,
                        tbApiConfig.getAdzoneId(),
                        4041L,
                        null));
    }

    @Override
    public PageBean<SmProductEntityEx> four(GoodsListForm goodsListForm) throws Exception {
        return tbSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                tbSdkComponent.optSearch(
                        goodsListForm,
                        tbApiConfig.getAdzoneId(),
                        3786L,
                        null));
    }

    @Override
    public PageBean<SmProductEntityEx> five(GoodsListForm goodsListForm) throws Exception {
        return tbSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                tbSdkComponent.optSearch(
                        goodsListForm,
                        tbApiConfig.getAdzoneId(),
                        13366L,
                        null));
    }
}
