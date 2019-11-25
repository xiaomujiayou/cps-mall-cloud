package com.xm.comment.module.mall.feign;

import com.xm.comment.annotation.LoginUser;
import com.xm.comment.module.mall.feign.fallback.MallFeignClientFallBack;
import com.xm.comment.response.Msg;
import com.xm.comment_serialize.module.mall.entity.SmConfigEntity;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.form.OrderIncrementListForm;
import com.xm.comment_serialize.module.mall.form.ProductDetailForm;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Date;

@FeignClient(value = "api-mall",fallback = MallFeignClientFallBack.class)
public interface MallFeignClient {

    @GetMapping("/config/one")
    public Msg<SmConfigEntity> getOneConfig(@RequestParam Integer userId,@RequestParam String configName,@RequestParam Integer configType);

    @GetMapping(value = "/product/detail")
    public Msg<SmProductEntity> getProductDetail(@RequestParam Integer platformType,@RequestParam String goodsId, @RequestParam BindingResult bindingResult, @RequestParam Integer userId);

    @PostMapping("/pid")
    public Msg<String> generatePid(@RequestParam Integer userId,@RequestParam Integer platformType);

    @GetMapping("/order/pdd/increment")
    public Msg<PageBean<SuOrderEntity>> getIncrement(@RequestParam Date startUpdateTime,@RequestParam Date endUpdateTime,@RequestParam Integer pageNum,@RequestParam Integer pageSize);

    @GetMapping("/order/time")
    public Msg<Date> getTime();
}
