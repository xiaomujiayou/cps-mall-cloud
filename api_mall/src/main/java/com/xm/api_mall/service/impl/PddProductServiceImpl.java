package com.xm.api_mall.service.impl;

import com.alibaba.fastjson.JSON;
import com.xm.api_mall.mapper.SmOptMapper;
import com.xm.api_mall.service.ProductApiService;
import com.xm.api_mall.service.ProductService;
import com.xm.comment.exception.GlobleException;
import com.xm.comment.module.user.feign.UserFeignClient;
import com.xm.comment.response.MsgEnum;
import com.xm.comment_serialize.module.mall.bo.ProductCriteriaBo;
import com.xm.comment_serialize.module.mall.bo.ShareLinkBo;
import com.xm.comment_serialize.module.mall.constant.BannerTypeEnum;
import com.xm.comment_serialize.module.mall.constant.PlatformTypeConstant;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.form.ProductListForm;
import com.xm.comment_serialize.module.user.entity.SuPidEntity;
import com.xm.comment_serialize.module.user.form.AddSearchForm;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("pddProductService")
public class PddProductServiceImpl implements ProductService {

    @Resource(name = "pddProductApiService")
    private ProductApiService productApiService;

    @Autowired
    private SmOptMapper smOptMapper;

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public PageBean<SmProductEntity> optionList(Integer userId, ProductListForm productListForm) throws Exception {
        if(productListForm.getOptionId() == null || productListForm.getOptionId() == 0)
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"optionId 不能为空");
        ProductCriteriaBo productCriteriaBo = new ProductCriteriaBo();
        productCriteriaBo.setUserId(userId);
        productCriteriaBo.setPageNum(productListForm.getPageNum());
        productCriteriaBo.setPageSize(productListForm.getPageSize());
        productCriteriaBo.setOptionId(Integer.valueOf(smOptMapper.selectByPrimaryKey(productListForm.getOptionId()).getPddOptId()));
        return productApiService.getProductByCriteria(productCriteriaBo);
    }

    @Override
    public PageBean<SmProductEntity> similarList(Integer userId, ProductListForm productListForm) {
        return null;
    }

    @Override
    public PageBean<SmProductEntity> bestList(Integer userId, ProductListForm productListForm) throws Exception {
        ProductCriteriaBo productCriteriaBo = new ProductCriteriaBo();
        if(productListForm.getActivityTags() != null && !productListForm.getActivityTags().isEmpty())
            productCriteriaBo.setActivityTags(productListForm.getActivityTags());
        productCriteriaBo.setUserId(userId);
        productCriteriaBo.setPageNum(productListForm.getPageNum());
        productCriteriaBo.setPageSize(productListForm.getPageSize());
        return productApiService.getProductByCriteria(productCriteriaBo);
    }

    @Override
    public PageBean<SmProductEntity> keywordList(Integer userId, ProductListForm productListForm) throws Exception {
        if(productListForm.getKeywords() == null || productListForm.getKeywords().trim().equals(""))
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"keywords 不能为空");
        //添加搜索历史
        if(productListForm.getPageNum() == 1 && userId != null){
            //只在搜索第一页添加
            AddSearchForm addSearchForm = new AddSearchForm();
            addSearchForm.setKeyWords(productListForm.getKeywords());
            userFeignClient.addSearch(userId,addSearchForm);
        }

        ProductCriteriaBo productCriteriaBo = new ProductCriteriaBo();
        productCriteriaBo.setUserId(userId);
        productCriteriaBo.setPageNum(productListForm.getPageNum());
        productCriteriaBo.setPageSize(productListForm.getPageSize());
        productCriteriaBo.setOrderBy(productListForm.getSort());
        if(productListForm.getMinPrice() != null && productListForm.getMaxPrice() != null){
            productCriteriaBo.setMinPrice(productListForm.getMinPrice());
            productCriteriaBo.setMaxPrice(productListForm.getMaxPrice());
        }
        productCriteriaBo.setKeyword(productListForm.getKeywords());

        return productApiService.getProductByCriteria(productCriteriaBo);
    }

    @Override
    public PageBean<SmProductEntity> hotList(Integer userId, ProductListForm productListForm) throws Exception {
        //热销榜
        return productApiService.getTopGoodsList(2,productListForm.getPageNum(),productListForm.getPageSize());
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
        return productApiService.getThemeGoodsList(productListForm.getThemeId());
    }

    @Override
    public PageBean<SmProductEntity> recommendList(Integer userId, ProductListForm productListForm) throws Exception {
        return productApiService.getRecommendGoodsList(productListForm.getChannelType(),productListForm.getPageNum(),productListForm.getPageSize());
    }

    @Override
    public List<SmBannerEntity> themes() throws Exception {
        //添加拼多多主题商品活动
        List<SmBannerEntity> pddThemes = productApiService.getThemeList().stream().map(o->{
            SmBannerEntity smBannerEntity = new SmBannerEntity();
            smBannerEntity.setId(o.getId());
            smBannerEntity.setType(BannerTypeEnum.HOME.getType());
            smBannerEntity.setImg(o.getImageUrl());
            smBannerEntity.setName(o.getName());
            smBannerEntity.setUrl(String.format("/pages/theme/index?themeId=%s&platformType=%s&themeName=%s&themeImg=%s",o.getId(),PlatformTypeConstant.PDD,o.getName(),o.getImageUrl()));
            smBannerEntity.setTarget(2);
            return smBannerEntity;
        }).collect(Collectors.toList());
        return pddThemes;
    }

    @Override
    public List<SmProductEntity> details(List<String> goodsIds) throws Exception {
        List<Long> goodsIdList = goodsIds.stream().map(o->{return Long.valueOf(o);}).collect(Collectors.toList());
        return productApiService.details(goodsIdList);
    }

    @Override
    public SmProductEntity detail(String goodsId) throws Exception {
        return productApiService.detail(Long.valueOf(goodsId));
    }

    @Override
    public ShareLinkBo saleInfo(Integer userId, Integer appType, Integer fromUser, String goodsId) throws Exception {
        Map<String,Object> customParams = new HashMap<>();
        customParams.put("userId",userId);
        customParams.put("appType",appType);
        customParams.put("fromUser",fromUser);
        SuPidEntity suPidEntity = userFeignClient.getPid(userId, PlatformTypeConstant.PDD).getData();
        return productApiService.getShareLink(JSON.toJSONString(customParams),suPidEntity.getPId(),Long.valueOf(goodsId));
    }

    @Override
    public String generatePid(Integer userId) throws Exception {
        return productApiService.generatePid(userId);
    }
}
