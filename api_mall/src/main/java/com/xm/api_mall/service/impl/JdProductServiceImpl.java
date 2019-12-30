package com.xm.api_mall.service.impl;

import com.xm.api_mall.mapper.SmOptMapper;
import com.xm.api_mall.service.ProductApiService;
import com.xm.api_mall.service.ProductService;
import com.xm.comment.exception.GlobleException;
import com.xm.comment.response.MsgEnum;
import com.xm.comment_serialize.module.mall.bo.ProductCriteriaBo;
import com.xm.comment_serialize.module.mall.bo.ShareLinkBo;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.form.ProductListForm;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("jdProductService")
public class JdProductServiceImpl implements ProductService {


    @Override
    public PageBean<SmProductEntity> optionList(Integer userId, ProductListForm productListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntity> similarList(Integer userId, ProductListForm productListForm) {
        return null;
    }

    @Override
    public PageBean<SmProductEntity> bestList(Integer userId, ProductListForm productListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntity> keywordList(Integer userId, ProductListForm productListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntity> hotList(Integer userId, ProductListForm productListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntity> customList(Integer userId, ProductListForm productListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntity> likeList(Integer userId, ProductListForm productListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntity> themeList(Integer userId, ProductListForm productListForm) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntity> recommendList(Integer userId, ProductListForm productListForm) throws Exception {
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
    public SmProductEntity detail(String goodsId) {
        return null;
    }

    @Override
    public ShareLinkBo saleInfo(Integer userId, Integer appType, Integer fromUser, String goodsId) throws Exception {
        return null;
    }

    @Override
    public String generatePid(Integer userId) throws Exception {
        return null;
    }
}
