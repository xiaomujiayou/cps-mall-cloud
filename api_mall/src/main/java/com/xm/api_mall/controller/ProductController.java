package com.xm.api_mall.controller;

import com.xm.api_mall.component.PlatformContext;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.module.user.feign.UserFeignClient;
import com.xm.comment.response.Msg;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.form.GetProductSaleInfoForm;
import com.xm.comment_serialize.module.mall.form.ProductDetailForm;
import com.xm.comment_serialize.module.mall.form.ProductListForm;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private PlatformContext productContext;
    @Autowired
    private UserFeignClient userFeignClient;

    /**
     * 推荐商品列表
     * @return
     */
    @PostMapping("/list")
    public Msg<Object> getProductList(@RequestBody @Valid ProductListForm productListForm, BindingResult bindingResult, @LoginUser(necessary = false) Integer userId) throws Exception {
        return R.sucess(
                productContext
                .platformType(productListForm.getPlatformType())
                .listType(productListForm.getListType())
                .invoke(userId,productListForm));
    }

    /**
     * 获取商品详情
     * @return
     */
    @GetMapping("/detail")
    public Msg<SmProductEntity> getProductDetail(@Valid ProductDetailForm productDetailForm, BindingResult bindingResult, @LoginUser(necessary = false) Integer userId) throws Exception {
        userFeignClient.addProductHistory(userId,productDetailForm.getPlatformType(),productDetailForm.getGoodsId());
        return R.sucess(productContext
                .platformType(productDetailForm.getPlatformType())
                .getService()
                .detail(productDetailForm.getGoodsId()));
    }

    @GetMapping("/sale")
    public Msg getProductSaleInfo(@LoginUser Integer userId,@Valid GetProductSaleInfoForm productSaleInfoForm) throws Exception {
        return R.sucess(productContext
                .platformType(productSaleInfoForm.getPlatformType())
                .getService()
                .saleInfo(
                        userId,
                        productSaleInfoForm.getAppType(),
                        productSaleInfoForm.getFromUser(),
                        productSaleInfoForm.getGoodsId()));
    }


}
