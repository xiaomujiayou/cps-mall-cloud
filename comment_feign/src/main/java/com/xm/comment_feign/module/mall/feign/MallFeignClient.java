package com.xm.comment_feign.module.mall.feign;

import com.xm.comment_feign.module.mall.feign.fallback.MallFeignClientFallBack;
import com.xm.comment_serialize.module.mall.bo.ProductIndexBo;
import com.xm.comment_serialize.module.mall.entity.SmConfigEntity;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.CalcProfitForm;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@FeignClient(value = "api-mall",fallback = MallFeignClientFallBack.class)
public interface MallFeignClient {

    @GetMapping("/config/one")
    public SmConfigEntity getOneConfig(@RequestParam Integer userId,@RequestParam String configName,@RequestParam Integer configType);

    @GetMapping(value = "/product/detail")
    public SmProductEntity getProductDetail(@RequestParam Integer platformType,@RequestParam String goodsId, @RequestParam BindingResult bindingResult, @RequestParam Integer userId);

    @GetMapping("/order/pdd/increment")
    public PageBean<SuOrderEntity> getIncrement(@RequestParam Date startUpdateTime,@RequestParam Date endUpdateTime,@RequestParam Integer pageNum,@RequestParam Integer pageSize);

    @GetMapping("/order/time")
    public Date getTime();

    @GetMapping("/product/details")
    public List<SmProductEntity> getProductDetails(@RequestParam Integer platformType,@RequestParam List<String> goodsIds);

    @PostMapping(value = "/product/details" ,consumes = "application/json")
    public List<SmProductEntity> getProductDetails(@RequestBody List<ProductIndexBo> productIndexBos);

    @PostMapping(value = "/profit/calc" ,consumes = "application/json")
    public List<SmProductEntityEx> calc(@RequestBody CalcProfitForm calcProfitForm);
}
