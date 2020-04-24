package com.xm.api_mall.service.api.impl.abs;

import cn.hutool.core.bean.BeanUtil;
import com.xm.api_mall.service.api.GoodsListService;
import com.xm.api_mall.service.api.OptionService;
import com.xm.comment_serialize.module.mall.bo.ProductCriteriaBo;
import com.xm.comment_serialize.module.mall.entity.SmOptEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.*;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.comment_utils.response.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GoodsListServiceAbs implements GoodsListService {

    @Autowired
    private OptionService optionService;

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

    /**
     * listform 转换为 criteriabo
     * @param listForm
     * @return
     */
    protected ProductCriteriaBo convertCriteriaBo(GoodsListForm listForm){
        ProductCriteriaBo criteriaBo = new ProductCriteriaBo();
        criteriaBo.setUserId(listForm.getUserId());
        criteriaBo.setAppType(listForm.getAppType());
        criteriaBo.setPageNum(listForm.getPageNum());
        criteriaBo.setPageSize(listForm.getPageSize());
        criteriaBo.setPid(listForm.getPid());
        criteriaBo.setIp(listForm.getIp());
        if(listForm instanceof OptionGoodsListForm){
            Integer optionId = ((OptionGoodsListForm)listForm).getOptionId();
            criteriaBo.setOptionId(optionId == null ? null : optionId.toString());
        }
        if(listForm instanceof KeywordGoodsListForm){
            KeywordGoodsListForm keywordGoodsListForm = (KeywordGoodsListForm)listForm;
            criteriaBo.setKeyword(keywordGoodsListForm.getKeywords());
            criteriaBo.setOrderBy(keywordGoodsListForm.getSort());
            criteriaBo.setMinPrice(keywordGoodsListForm.getMinPrice());
            criteriaBo.setMaxPrice(keywordGoodsListForm.getMaxPrice());
            criteriaBo.setHasCoupon(keywordGoodsListForm.getHasCoupon());
            criteriaBo.setParcels(keywordGoodsListForm.getParcels());
            criteriaBo.setIsTmall(keywordGoodsListForm.getIsTmall());
            criteriaBo.setLocation(keywordGoodsListForm.getLocation());
        }
        if(listForm instanceof GoodsDetailsForm){
            GoodsDetailsForm goodsDetailsForm = (GoodsDetailsForm)listForm;
            criteriaBo.setGoodsIdList(goodsDetailsForm.getGoodsIds());
        }
        return criteriaBo;
    }

    /**
     * 转换optionId为类目名称
     * @param listForm
     * @return
     */
    protected String convertOptionToName(OptionGoodsListForm listForm){
        OptionForm optionForm = new OptionForm();
        BeanUtil.copyProperties(listForm,optionForm);
        optionForm.setTargetOptId(listForm.getOptionId());
        List<SmOptEntity> optEntities = optionService.allParentList(optionForm);
        if(optEntities == null)
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR,"无效optId:" + listForm.getOptionId());
        String keyWords = String.join(" ",optEntities.stream().map(SmOptEntity::getName).collect(Collectors.toList()));
        return keyWords;
    }
}
