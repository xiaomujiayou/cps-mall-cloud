package com.xm.comment.module.mall.feign.fallback;

import com.xm.comment.module.mall.feign.MallFeignClient;
import com.xm.comment.response.Msg;
import com.xm.comment.response.MsgEnum;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.mall.entity.SmConfigEntity;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.form.ProductDetailForm;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.List;

@Component
public class MallFeignClientFallBack implements MallFeignClient {

    @Override
    public Msg<SmConfigEntity> getOneConfig(Integer userId, String configName, Integer configType) {
        return R.error(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public Msg<SmProductEntity> getProductDetail(Integer platformType, String goodsId, BindingResult bindingResult, Integer userId) {
        return R.error(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public Msg<String> generatePid(Integer userId, Integer platformType) {
        return R.error(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public Msg<PageBean<SuOrderEntity>> getIncrement(Date startUpdateTime, Date endUpdateTime, Integer pageNum, Integer pageSize) {
        return R.error(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public Msg<Date> getTime() {
        return R.error(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public Msg<List<SmProductEntity>> getProductDetails(Integer platformType, List<String> goodsIds) {
        return R.error(MsgEnum.SERVICE_AVAILABLE);
    }

}
