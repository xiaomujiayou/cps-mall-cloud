package com.xm.api_mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xm.api_mall.service.OptionService;
import com.xm.api_mall.service.ProductApiService;
import com.xm.api_mall.service.ProductService;
import com.xm.api_mall.service.ProfitService;
import com.xm.comment.module.user.feign.UserFeignClient;
import com.xm.comment_serialize.module.mall.bo.ProductCriteriaBo;
import com.xm.comment_serialize.module.mall.bo.ShareLinkBo;
import com.xm.comment_serialize.module.mall.constant.GoodsSortContant;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import com.xm.comment_serialize.module.mall.entity.SmOptEntity;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.GetProductSaleInfoForm;
import com.xm.comment_serialize.module.mall.form.ProductListForm;
import com.xm.comment_serialize.module.mall.vo.SmProductSimpleVo;
import com.xm.comment_serialize.module.user.form.AddSearchForm;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.comment_utils.response.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("mgjProductService")
public class MgjProductServiceImpl implements ProductService {

    @Resource(name = "mgjProductApiService")
    private ProductApiService productApiService;
    @Autowired
    private ProfitService profitService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public PageBean<SmProductEntityEx> optionList(Integer userId, String pid, ProductListForm productListForm) throws Exception {
        if(productListForm.getOptionId() == null)
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"goodsId 不能为空");
        List<SmOptEntity> optEntities = optionService.getAllParentOption(userId,productListForm.getOptionId());
        if(optEntities == null)
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR,"无效optId:"+productListForm.getOptionId());
        String keyWords = String.join(" ",optEntities.stream().map(SmOptEntity::getName).collect(Collectors.toList()));
        productListForm.setKeywords(keyWords);
        return keyworkSearch(userId,pid,productListForm);
    }

    @Override
    public PageBean<SmProductEntityEx> similarList(Integer userId, String pid, ProductListForm productListForm) throws Exception {
        if(StrUtil.isBlank(productListForm.getGoodsId()))
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"goodsId 不能为空");
        SmProductEntity smProductEntity = productApiService.detail(productListForm.getGoodsId(),pid);
        productListForm.setKeywords(smProductEntity.getName());
        productListForm.setSort(3);
        PageBean<SmProductEntityEx> productEntityExPageBean = keyworkSearch(userId,pid,productListForm);
        productEntityExPageBean.setList(productEntityExPageBean.getList().stream().filter(o-> !smProductEntity.getGoodsId().equals(o.getGoodsId())).collect(Collectors.toList()));
        return productEntityExPageBean;
    }
    private PageBean<SmProductEntityEx> keyworkSearch(Integer userId,String pid, ProductListForm productListForm) throws Exception {
        ProductCriteriaBo productCriteriaBo = new ProductCriteriaBo();
        productCriteriaBo.setPid(pid);
        productCriteriaBo.setUserId(userId);
        productCriteriaBo.setPageNum(productListForm.getPageNum());
        productCriteriaBo.setPageSize(productListForm.getPageSize());
        productCriteriaBo.setOrderBy(productListForm.getSort());
        productCriteriaBo.setHasCoupon(productListForm.getHasCoupon());
        if(productListForm.getMinPrice() != null && productListForm.getMaxPrice() != null){
            productCriteriaBo.setMinPrice(productListForm.getMinPrice());
            productCriteriaBo.setMaxPrice(productListForm.getMaxPrice());
        }
        productCriteriaBo.setKeyword(productListForm.getKeywords());
        return convertSmProductEntityEx(userId,productApiService.getProductByCriteria(productCriteriaBo));
    }
    @Override
    public PageBean<SmProductEntityEx> bestList(Integer userId, String pid, ProductListForm productListForm) throws Exception {
        ProductCriteriaBo productCriteriaBo = new ProductCriteriaBo();
        if(productListForm.getActivityTags() != null && !productListForm.getActivityTags().isEmpty())
            productCriteriaBo.setActivityTags(productListForm.getActivityTags());
        productCriteriaBo.setPid(pid);
        productCriteriaBo.setUserId(userId);
        productCriteriaBo.setPageNum(productListForm.getPageNum());
        productCriteriaBo.setPageSize(productListForm.getPageSize());
        productCriteriaBo.setHasCoupon(true);
        productCriteriaBo.setOrderBy(GoodsSortContant.SALES_DESC);
        return convertSmProductEntityEx(userId,productApiService.getProductByCriteria(productCriteriaBo));
    }
    private PageBean<SmProductEntityEx> convertSmProductEntityEx(Integer userId,PageBean<SmProductEntity> pageBean){
        List<SmProductEntityEx> list = profitService.calcProfit(pageBean.getList(),userId);
        PageBean<SmProductEntityEx> productEntityExPageBean = new PageBean<>();
        productEntityExPageBean.setList(list);
        productEntityExPageBean.setPageNum(pageBean.getPageNum());
        productEntityExPageBean.setPageSize(pageBean.getPageSize());
        productEntityExPageBean.setTotal(pageBean.getTotal());
        return productEntityExPageBean;
    }

    @Override
    public PageBean<SmProductEntityEx> keywordList(Integer userId, String pid, ProductListForm productListForm) throws Exception {
        if(productListForm.getKeywords() == null || productListForm.getKeywords().trim().equals(""))
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"keywords 不能为空");
        //添加搜索历史
        if(productListForm.getPageNum() == 1 && userId != null){
            //只在搜索第一页添加
            AddSearchForm addSearchForm = new AddSearchForm();
            addSearchForm.setKeyWords(productListForm.getKeywords());
            userFeignClient.addSearch(userId,addSearchForm);
        }
        return keyworkSearch(userId,pid,productListForm);
    }

    @Override
    public PageBean<SmProductEntityEx> hotList(Integer userId, String pid, ProductListForm productListForm) throws Exception {
        return bestList(userId,pid,productListForm);
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
        ProductCriteriaBo productCriteriaBo = new ProductCriteriaBo();
        if(productListForm.getActivityTags() != null && !productListForm.getActivityTags().isEmpty())
            productCriteriaBo.setActivityTags(productListForm.getActivityTags());
        productCriteriaBo.setPid(pid);
        productCriteriaBo.setUserId(userId);
        productCriteriaBo.setPageNum(productListForm.getPageNum());
        productCriteriaBo.setPageSize(productListForm.getPageSize());
        productCriteriaBo.setHasCoupon(true);
        productCriteriaBo.setOrderBy(GoodsSortContant.PRICE_ASC);
        return convertSmProductEntityEx(userId,productApiService.getProductByCriteria(productCriteriaBo));
    }

    @Override
    public List<SmBannerEntity> themes() throws Exception {
        return null;
    }

    @Override
    public List<SmProductEntity> details(List<String> goodsIds) throws Exception {
        return productApiService.details(goodsIds);
    }

    @Override
    public SmProductEntityEx detail(String goodsId, String pid, Integer userId, Integer shareUserId) throws Exception {
        SmProductEntity smProductEntity = productApiService.detail(goodsId,pid);
        return profitService.calcProfit(smProductEntity,userId,shareUserId != null,shareUserId);
    }

    @Override
    public SmProductSimpleVo basicDetail(Long goodsId) throws Exception {
        return null;
    }


    @Override
    public ShareLinkBo saleInfo(Integer userId, String pid, GetProductSaleInfoForm productSaleInfoForm) throws Exception {
//        Map<String,Object> customParams = new HashMap<>();
//        customParams.put("userId",userId);
//        customParams.put("appType",appType);
//        customParams.put("fromUser",fromUser);
//        return productApiService.getShareLink(JSON.toJSONString(customParams),pid,Long.valueOf(goodsId));
        return null;
    }
}
