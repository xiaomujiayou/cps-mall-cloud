package com.xm.api_user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xm.api_user.service.ProductService;
import com.xm.api_user.service.ShareService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.module.mall.feign.MallFeignClient;
import com.xm.comment.response.Msg;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.*;
import com.xm.comment_utils.mybatis.PageBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private MallFeignClient mallFeignClient;

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
        if(productCollectForm.getShareUserId() != null && userId.equals(productCollectForm.getShareUserId()))
            productCollectForm.setShareUserId(null);
        productService.collect(userId,productCollectForm.getPlatformType(),productCollectForm.getGoodsId(),productCollectForm.getShareUserId(),productCollectForm.getIsCollect());
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
    public Msg<PageBean<SmProductEntityEx>> get(@LoginUser Integer userId, @Valid GetProductForm getProductForm,BindingResult bindingResult){
        PageBean<SmProductEntity> pageBean = productService.getUserProduct(
                userId,
                getProductForm.getPageNum(),
                getProductForm.getPageSize(),
                getProductForm.getSuProductType());
        Map<String,Integer> shareUserMap = pageBean.getList().stream().filter(o->{return ((SmProductEntityEx)o).getShareUserId() != null;}).collect(Collectors.toMap(SmProductEntity::getGoodsId,o->{return  ((SmProductEntityEx)o).getShareUserId();}));

        CalcProfitForm calcProfitForm = new CalcProfitForm();
        calcProfitForm.setUserId(userId);
        calcProfitForm.setSmProductEntities(pageBean.getList());
        List<SmProductEntityEx> smProductEntityExes = mallFeignClient.calc(calcProfitForm).getData();
        smProductEntityExes.stream().forEach(o->{
            o.setShareUserId(shareUserMap.get(o.getGoodsId()));
        });
        PageBean<SmProductEntityEx> smProductEntityExPageBean = new PageBean<>();
        smProductEntityExPageBean.setList(smProductEntityExes);
        smProductEntityExPageBean.setPageNum(pageBean.getPageNum());
        smProductEntityExPageBean.setPageSize(pageBean.getPageSize());
        smProductEntityExPageBean.setTotal(pageBean.getTotal());
        return R.sucess(smProductEntityExPageBean);
    }

    /**
     * 添加一条历史记录
     * @param userId
     * @return
     */
    @PostMapping("/history")
    public Msg get(@LoginUser Integer userId,@RequestBody @Valid AddUserHistoryForm addUserHistoryForm,BindingResult bindingResult){
        if(userId != null) {
            Integer shareUserId = addUserHistoryForm.getShareUserId()== null?null:userId.equals(addUserHistoryForm.getShareUserId())?null:addUserHistoryForm.getShareUserId();
            productService.addHistory(userId,addUserHistoryForm.getPlatformType(),addUserHistoryForm.getGoodsId(),shareUserId);
        }
        //添加分享记录
        if(addUserHistoryForm.getShareUserId() != null && !addUserHistoryForm.getShareUserId().equals(userId)){
            shareService.show(addUserHistoryForm.getShareUserId(),addUserHistoryForm.getGoodsId(),addUserHistoryForm.getPlatformType());
        }
        return R.sucess();
    }

}
