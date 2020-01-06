package com.xm.api_mall.service.impl;

import com.xm.api_mall.service.ProductService;
import com.xm.comment_serialize.module.mall.bo.ShareLinkBo;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.ProductListForm;
import com.xm.comment_serialize.module.mall.vo.SmProductSimpleVo;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mgjProductService")
public class MgjProductServiceImpl implements ProductService {


    @Override
    public PageBean<SmProductEntityEx> optionList(Integer userId, String pid, ProductListForm productListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> similarList(Integer userId, String pid, ProductListForm productListForm) {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> bestList(Integer userId, String pid, ProductListForm productListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> keywordList(Integer userId, String pid, ProductListForm productListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> hotList(Integer userId, String pid, ProductListForm productListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> customList(Integer userId, String pid, ProductListForm productListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> likeList(Integer userId, String pid, ProductListForm productListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> themeList(Integer userId, String pid, ProductListForm productListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntityEx> recommendList(Integer userId, String pid, ProductListForm productListForm) throws Exception {
        return null;
    }

    @Override
    public List<SmBannerEntity> themes() throws Exception {
        return null;
    }

    @Override
    public List<SmProductEntity> details(List<String> goodsIds) throws Exception {
        return null;
    }

    @Override
    public SmProductEntityEx detail(String goodsId, String pid, Integer userId, Integer shareUserId) throws Exception {
        return null;
    }

    @Override
    public SmProductSimpleVo basicDetail(Long goodsId) throws Exception {
        return null;
    }


    @Override
    public ShareLinkBo saleInfo(Integer userId, String pid, Integer appType, Integer fromUser, String goodsId) throws Exception {
        return null;
    }
}
