package com.xm.comment_feign.module.mall.feign.fallback;

import com.xm.comment_feign.module.mall.feign.MallFeignClient;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_serialize.module.mall.bo.ProductIndexBo;
import com.xm.comment_serialize.module.mall.entity.SmConfigEntity;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.CalcProfitForm;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.List;

@Component
public class MallFeignClientFallBack implements MallFeignClient {


    @Override
    public SmConfigEntity getOneConfig(Integer userId, String configName, Integer configType) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public SmProductEntity getProductDetail(Integer platformType, String goodsId, BindingResult bindingResult, Integer userId) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public PageBean<SuOrderEntity> getIncrement(Date startUpdateTime, Date endUpdateTime, Integer pageNum, Integer pageSize) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public Date getTime() {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public List<SmProductEntity> getProductDetails(Integer platformType, List<String> goodsIds) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public List<SmProductEntity> getProductDetails(List<ProductIndexBo> productIndexBos) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public List<SmProductEntityEx> calc(CalcProfitForm calcProfitForm) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }
}
