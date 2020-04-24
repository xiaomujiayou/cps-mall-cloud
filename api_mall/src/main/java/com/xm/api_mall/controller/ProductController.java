package com.xm.api_mall.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xm.api_mall.aspect.DispatchServiceAspect;
import com.xm.api_mall.exception.ApiCallException;
import com.xm.api_mall.service.api.GoodsListService;
import com.xm.api_mall.service.api.GoodsService;
import com.xm.api_mall.service.api.OptGoodsListService;
import com.xm.api_mall.utils.TextToGoodsUtils;
import com.xm.comment.annotation.AppType;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.annotation.Pid;
import com.xm.comment.annotation.PlatformType;
import com.xm.comment_feign.module.wind.feign.WindFeignClient;
import com.xm.comment_serialize.form.BaseForm;
import com.xm.comment_serialize.module.mall.bo.ShareLinkBo;
import com.xm.comment_serialize.module.mall.constant.PlatformTypeConstant;
import com.xm.comment_serialize.module.mall.form.*;
import com.xm.comment_serialize.module.mall.bo.ProductIndexBo;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.vo.SmProductVo;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.comment_utils.response.MsgEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.lang.model.element.NestingKind;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ProductController productController;
    @Autowired
    private DispatchServiceAspect dispatchServiceAspect;
    @Resource(name = "myExecutor")
    private ThreadPoolTaskExecutor executor;
    @Autowired
    private WindFeignClient windFeignClient;

    /**
     * 商品列表
     * @return
     */
    @PostMapping("/list")
    public Object getProductList(@LoginUser @Pid @PlatformType @AppType BaseForm baseForm,@RequestBody JSONObject params) throws Exception {
        params.putAll((JSONObject)JSON.toJSON(baseForm));
        PageBean<SmProductEntityEx> pageBean = (PageBean<SmProductEntityEx>)dispatchServiceAspect.getList(
                params,
                GoodsListService.class,
                OptGoodsListService.class);
        pageBean.setList(windFeignClient.productCheck(pageBean.getList()));
        List<SmProductVo> list = pageBean.getList().stream().map(o->{
            SmProductVo smProductVo = new SmProductVo();
            BeanUtil.copyProperties(o,smProductVo);
            return smProductVo;
        }).collect(Collectors.toList());
        PageBean<SmProductVo> productVoPageBean = new PageBean<>();
        productVoPageBean.setList(list);
        productVoPageBean.setPageNum(pageBean.getPageNum());
        productVoPageBean.setPageSize(pageBean.getPageSize());
        productVoPageBean.setTotal(pageBean.getTotal());
        return productVoPageBean;
    }



    /**
     * 获取商品详情
     * @return
     */
    @GetMapping("/detail")
    public SmProductVo getProductDetail(@Valid @PlatformType @LoginUser(necessary = false) @Pid(necessary = false) GoodsDetailForm goodsDetailForm, BindingResult bindingResult) throws Exception {
        return praseDetailVo(productController.getDetailEx(goodsDetailForm));
    }

    public SmProductEntityEx getDetailEx(GoodsDetailForm goodsDetailForm) throws Exception {
        SmProductEntityEx smProductEntityEx = goodsService.detail(goodsDetailForm);
        return smProductEntityEx;
    }

    private SmProductVo praseDetailVo(SmProductEntityEx smProductEntityEx){
        List<SmProductEntityEx> smProductEntityExes = windFeignClient.productCheck(Arrays.asList(smProductEntityEx));
        if(smProductEntityExes != null && !smProductEntityExes.isEmpty())
            smProductEntityEx = smProductEntityExes.get(0);
        SmProductVo smProductVo = new SmProductVo();
        BeanUtil.copyProperties(smProductEntityEx,smProductVo);
        return smProductVo;
    }

    /**
     * 批量获取商品详情
     * @return
     */
    @GetMapping("/details")
    public List<SmProductEntity> getProductDetails(Integer platformType,@RequestParam("goodsIds") List<String> goodsIds) throws Exception {
        if(goodsIds == null || goodsIds.isEmpty())
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR);
        GoodsDetailsForm goodsDetailsForm = new GoodsDetailsForm();
        goodsDetailsForm.setPlatformType(platformType);
        goodsDetailsForm.setGoodsIds(goodsIds);
        return goodsService.details(goodsDetailsForm);
    }

    /**
     * 批量获取商品详情
     * @return
     */
    @PostMapping("/details")
    public List<SmProductEntity> getProductDetails(@RequestBody List<ProductIndexBo> productIndexBos) throws Exception {
        Map<Integer, List<ProductIndexBo>> group = productIndexBos.stream().collect(Collectors.groupingBy(ProductIndexBo::getPlatformType));
        List<Future<List<SmProductEntity>>> futures = new ArrayList<>();
        for (Map.Entry<Integer, List<ProductIndexBo>> integerListEntry : group.entrySet()) {
            Future<List<SmProductEntity>> listFuture = executor.submit(new Callable<List<SmProductEntity>>() {
                @Override
                public List<SmProductEntity> call() throws Exception {
                    GoodsDetailsForm goodsDetailsForm = new GoodsDetailsForm();
                    goodsDetailsForm.setPlatformType(integerListEntry.getKey());
                    goodsDetailsForm.setGoodsIds(integerListEntry.getValue()
                            .stream()
                            .map(ProductIndexBo::getGoodsId)
                            .collect(Collectors.toList()));
                    return goodsService.details(goodsDetailsForm);
                }
            });
            futures.add(listFuture);
        }
        //合并结果
        List<SmProductEntity> result = new ArrayList<>();
        futures.forEach(o ->{
            try {
                List<SmProductEntity> smProductEntities = o.get();
                result.addAll(smProductEntities);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    @GetMapping("/sale")
    public ShareLinkBo getProductSaleInfo( @Valid @LoginUser @PlatformType @Pid SaleInfoForm saleInfoForm) throws Exception {
        if(saleInfoForm.getUserId().equals(saleInfoForm.getShareUserId()))
            saleInfoForm.setShareUserId(null);
        return goodsService.saleInfo(saleInfoForm);
    }

    @GetMapping("/url/parse")
    public TextToGoodsUtils.GoodsSpec parseUrl( @Valid @LoginUser(necessary = false) @Pid(necessary = false) UrlParseForm urlParseForm,BindingResult bindingResult) throws Exception {
        TextToGoodsUtils.GoodsSpec goodsSpec = TextToGoodsUtils.parse(urlParseForm.getUrl());
        if(goodsSpec.getParseType() == 1){
            //解析为具体商品
            if(goodsSpec.getPlatformType() == PlatformTypeConstant.PDD){
                try {
                    GoodsDetailForm goodsDetailForm = new GoodsDetailForm();
                    BeanUtil.copyProperties(urlParseForm,goodsDetailForm);
                    goodsDetailForm.setGoodsId(goodsSpec.getGoodsId());
                    goodsDetailForm.setPlatformType(goodsSpec.getPlatformType());
                    goodsSpec.setGoodsInfo(praseDetailVo(productController.getDetailEx(goodsDetailForm)));
                }catch (ApiCallException e){
                    goodsSpec.setParseType(4);
//                    BaseGoodsDetailForm baseGoodsDetailForm = new BaseGoodsDetailForm();
//                    BeanUtil.copyProperties(urlParseForm,baseGoodsDetailForm);
//                    baseGoodsDetailForm.setGoodsId(goodsSpec.getGoodsId());
//                    goodsSpec.setSimpleInfo(goodsService.basicDetail(baseGoodsDetailForm));
                }
            }else if(Arrays.asList(PlatformTypeConstant.MGJ,PlatformTypeConstant.WPH).contains(goodsSpec.getPlatformType())){
                try {
                    GoodsDetailForm goodsDetailForm = new GoodsDetailForm();
                    BeanUtil.copyProperties(urlParseForm,goodsDetailForm);
                    goodsDetailForm.setGoodsId(goodsSpec.getGoodsId());
                    goodsDetailForm.setPlatformType(goodsSpec.getPlatformType());
                    goodsSpec.setGoodsInfo(praseDetailVo(productController.getDetailEx(goodsDetailForm)));
                }catch (Exception e){
                    goodsSpec.setParseType(4);
                }
            }
        }
        return goodsSpec;
    }
}
