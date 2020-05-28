package com.xm.api_mall.service.api.impl.pdd;

import cn.hutool.core.util.StrUtil;
import com.xm.api_mall.component.PddSdkComponent;
import com.xm.api_mall.mapper.SmOptMapper;
import com.xm.api_mall.service.ProfitService;
import com.xm.api_mall.service.api.GoodsListService;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.form.*;
import com.xm.comment_feign.module.user.feign.UserFeignClient;
import com.xm.comment_serialize.module.mall.bo.ProductCriteriaBo;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.user.form.AddSearchForm;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.comment_utils.response.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service("pddGoodsListService")
public class GoodsListServiceImpl implements GoodsListService {
    @Autowired
    private PddSdkComponent pddSdkComponent;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private SmOptMapper smOptMapper;

    @Override
    public PageBean<SmProductEntityEx> index(GoodsListForm goodsListForm) throws Exception {
        return pddSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                pddSdkComponent.getRecommendGoodsList(
                        goodsListForm.getPid(),
                        0,
                        goodsListForm.getPageNum(),
                        goodsListForm.getPageSize()));
    }

    @Override
    public PageBean<SmProductEntityEx> recommend(GoodsListForm goodsListForm) throws Exception {
        //热销榜
        return pddSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                pddSdkComponent.getTopGoodsList(
                        2,
                        goodsListForm.getPid(),
                        goodsListForm.getPageNum(),
                        goodsListForm.getPageSize()));
    }

    @Override
    public PageBean<SmProductEntityEx> my(GoodsListForm goodsListForm) throws Exception {
        ProductCriteriaBo productCriteriaBo = new ProductCriteriaBo();
        productCriteriaBo.setPid(goodsListForm.getPid());
        productCriteriaBo.setUserId(goodsListForm.getUserId());
        productCriteriaBo.setPageNum(goodsListForm.getPageNum());
        productCriteriaBo.setPageSize(goodsListForm.getPageSize());
        return pddSdkComponent.convertSmProductEntityEx(
                goodsListForm.getUserId(),
                pddSdkComponent.getProductByCriteria(productCriteriaBo));
    }

    @Override
    public PageBean<SmProductEntityEx> keyword(KeywordGoodsListForm keywordGoodsListForm) throws Exception {
        if(keywordGoodsListForm.getKeywords() == null || keywordGoodsListForm.getKeywords().trim().equals(""))
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"keywords 不能为空");
        //添加搜索历史
        if(keywordGoodsListForm.getPageNum() == 1 && keywordGoodsListForm.getUserId() != null){
            //只在搜索第一页添加
            AddSearchForm addSearchForm = new AddSearchForm();
            addSearchForm.setKeyWords(keywordGoodsListForm.getKeywords());
            userFeignClient.addSearch(keywordGoodsListForm.getUserId(),addSearchForm);
        }
        return keyworkSearch(keywordGoodsListForm.getUserId(),keywordGoodsListForm.getPid(),keywordGoodsListForm);
    }

    @Override
    public PageBean<SmProductEntityEx> option(OptionGoodsListForm optionGoodsListForm) throws Exception {
        ProductCriteriaBo productCriteriaBo = new ProductCriteriaBo();
        productCriteriaBo.setPid(optionGoodsListForm.getPid());
        productCriteriaBo.setUserId(optionGoodsListForm.getUserId());
        productCriteriaBo.setPageNum(optionGoodsListForm.getPageNum());
        productCriteriaBo.setPageSize(optionGoodsListForm.getPageSize());
        productCriteriaBo.setOptionId(
                        smOptMapper.selectByPrimaryKey(
                                optionGoodsListForm.getOptionId()).
                                getPddOptId());
        return pddSdkComponent.convertSmProductEntityEx(
                optionGoodsListForm.getUserId(),
                pddSdkComponent.getProductByCriteria(productCriteriaBo));
    }

    @Override
    public PageBean<SmProductEntityEx> similar(SimilarGoodsListForm similarGoodsListForm) throws Exception {
        if(StrUtil.isBlank(similarGoodsListForm.getGoodsId()))
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"goodsId 不能为空");
        SmProductEntity smProductEntity = pddSdkComponent.detail(
                similarGoodsListForm.getGoodsId(),
                similarGoodsListForm.getPid());
        KeywordGoodsListForm keywordGoodsListForm = new KeywordGoodsListForm();
        keywordGoodsListForm.setKeywords(smProductEntity.getName());
        keywordGoodsListForm.setSort(3);
        keywordGoodsListForm.setPageNum(similarGoodsListForm.getPageNum());
        keywordGoodsListForm.setPageSize(similarGoodsListForm.getPageSize());
        PageBean<SmProductEntityEx> productEntityExPageBean = keyworkSearch(
                similarGoodsListForm.getUserId(),
                similarGoodsListForm.getPid(),
                keywordGoodsListForm);
        productEntityExPageBean.setList(productEntityExPageBean.getList().stream().filter(o-> !smProductEntity.getGoodsId().equals(o.getGoodsId())).collect(Collectors.toList()));
        return productEntityExPageBean;
    }

    @Override
    public PageBean<SmProductEntityEx> mall(MallGoodsListForm mallGoodsListForm) throws Exception {
        return pddSdkComponent.convertSmProductEntityEx(
                mallGoodsListForm.getUserId(),
                pddSdkComponent.mallGoodsList(mallGoodsListForm));
    }

    private PageBean<SmProductEntityEx> keyworkSearch(Integer userId,String pid, KeywordGoodsListForm keywordGoodsListForm) throws Exception {
        ProductCriteriaBo productCriteriaBo = new ProductCriteriaBo();
        productCriteriaBo.setPid(pid);
        productCriteriaBo.setUserId(userId);
        productCriteriaBo.setPageNum(keywordGoodsListForm.getPageNum());
        productCriteriaBo.setPageSize(keywordGoodsListForm.getPageSize());
        productCriteriaBo.setOrderBy(keywordGoodsListForm.getSort());
        productCriteriaBo.setHasCoupon(keywordGoodsListForm.getHasCoupon());
        if(keywordGoodsListForm.getMinPrice() != null && keywordGoodsListForm.getMaxPrice() != null){
            productCriteriaBo.setMinPrice(keywordGoodsListForm.getMinPrice());
            productCriteriaBo.setMaxPrice(keywordGoodsListForm.getMaxPrice());
        }
        productCriteriaBo.setKeyword(keywordGoodsListForm.getKeywords());
        return pddSdkComponent.convertSmProductEntityEx(
                userId,
                pddSdkComponent.getProductByCriteria(productCriteriaBo));
    }
}
