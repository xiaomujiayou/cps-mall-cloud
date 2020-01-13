package com.xm.api_mall.component;

import com.xm.api_mall.service.ProductService;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_serialize.module.mall.constant.PlatformTypeConstant;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 商品服务相关
 */
@Component
public class PlatformContext {

    @Resource(name = "pddProductService")
    private ProductService pddProductService;
    @Resource(name = "jdProductService")
    private ProductService jdProductService;
    @Resource(name = "mgjProductService")
    private ProductService mgjProductService;
    @Resource(name = "tbProductService")
    private ProductService tbProductService;

    public ProductListContext platformType(Integer platformType){
        switch (platformType){
            case PlatformTypeConstant.PDD :
                return new ProductListContext(pddProductService);
            case PlatformTypeConstant.JD :
                return new ProductListContext(jdProductService);
            case PlatformTypeConstant.MGJ :
                return new ProductListContext(mgjProductService);
            case PlatformTypeConstant.TB :
                return new ProductListContext(tbProductService);
            default:{
                throw new GlobleException(MsgEnum.TYPE_NOTFOUND_ERROR,"无效platformType："+platformType);
            }
        }
    }
}
