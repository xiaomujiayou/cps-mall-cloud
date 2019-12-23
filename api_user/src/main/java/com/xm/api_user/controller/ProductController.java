package com.xm.api_user.controller;

import com.xm.api_user.service.ProductService;
import com.xm.api_user.service.ShareService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.response.Msg;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.form.*;
import com.xm.comment_utils.mybatis.PageBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;

/**
 * 用户收藏/历史记录相关接口
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ShareService shareService;

    /**
     * 商品是否被收藏
     * @param userId
     * @param productCollectIsForm
     * @return
     */
    @GetMapping("/collect/is")
    public Msg collect(@LoginUser Integer userId, @Valid ProductCollectIsForm productCollectIsForm, BindingResult bindingResult){
        return R.sucess(productService.isCollect(userId,productCollectIsForm.getPlatformType(),productCollectIsForm.getGoodsId()));
    }

    /**
     * 收藏/取消收藏
     * @param userId
     * @param productCollectForm
     * @return
     */
    @PostMapping("/collect")
    public Msg collect(@LoginUser Integer userId, @Valid @RequestBody ProductCollectForm productCollectForm,BindingResult bindingResult){
        productService.collect(userId,productCollectForm.getPlatformType(),productCollectForm.getGoodsId(),productCollectForm.getIsCollect());
        return R.sucess();
    }

    /**
     * 删除一条历史记录
     * @param userId
     * @param id
     * @return
     */
    @DeleteMapping("/history/{id}")
    public Msg delHistory(@LoginUser Integer userId, @PathVariable Integer id){
        productService.delHistory(userId,id,false);
        return R.sucess();
    }

    /**
     * 删除所有历史记录
     * @param userId
     * @return
     */
    @DeleteMapping("/history")
    public Msg delHistory(@LoginUser Integer userId){
        productService.delHistory(userId,null,true);
        return R.sucess();
    }

    /**
     * 获取收藏/历史记录
     * @param userId
     * @param getProductForm
     * @param bindingResult
     * @return
     */
    @GetMapping
    public Msg<PageBean<SmProductEntity>> get(@LoginUser Integer userId, @Valid GetProductForm getProductForm,BindingResult bindingResult){
        return R.sucess(productService.getUserProduct(
                userId,
                getProductForm.getPageNum(),
                getProductForm.getPageSize(),
                getProductForm.getSuProductType()));
    }

    /**
     * 添加一条历史记录
     * @param userId
     * @return
     */
    @PostMapping("/history")
    public Msg get(@LoginUser Integer userId,@RequestBody @Valid AddUserHistoryForm addUserHistoryForm,BindingResult bindingResult){
        if(userId != null) {
            productService.addHistory(userId,addUserHistoryForm.getPlatformType(),addUserHistoryForm.getGoodsId());
        }
        //添加分享记录
//        if(addUserHistoryForm.getShareUserId() != null && !addUserHistoryForm.getShareUserId().equals("") && !addUserHistoryForm.getShareUserId().equals(userId)){
        if(addUserHistoryForm.getShareUserId() != null && !addUserHistoryForm.getShareUserId().equals("")){
            shareService.show(addUserHistoryForm.getShareUserId(),addUserHistoryForm.getGoodsId(),addUserHistoryForm.getPlatformType());
        }
        return R.sucess();
    }

}
