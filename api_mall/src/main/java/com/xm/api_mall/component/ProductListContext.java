package com.xm.api_mall.component;

import com.xm.api_mall.service.ProductService;
import com.xm.comment.exception.GlobleException;
import com.xm.comment.response.MsgEnum;
import com.xm.comment_serialize.module.mall.constant.ProductListTypeConstant;
import com.xm.comment_serialize.module.mall.form.ProductListForm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProductListContext<T> {

    private ProductService productService;
    private Method listMethod;

    public ProductListContext(ProductService productService) {
        this.productService = productService;
    }

    public ProductListContext listType(Integer listType) throws NoSuchMethodException {
        String methodName = null;
        switch (listType){
            case ProductListTypeConstant.BEST_LIST:{
                methodName = "bestList";
                break;
            }
            case ProductListTypeConstant.OPTION_LIST:{
                methodName = "optionList";
                break;
            }
            case ProductListTypeConstant.CUSTOM_LIST:{
                methodName = "customList";
                break;
            }
            case ProductListTypeConstant.LIKE_LIST:{
                methodName = "likeList";
                break;
            }
            case ProductListTypeConstant.SIMILAR_LIST:{
                methodName = "similarList";
                break;
            }
            case ProductListTypeConstant.HOT_LIST:{
                methodName = "hotList";
                break;
            }
            case ProductListTypeConstant.KEYWORD_LIST:{
                methodName = "keywordList";
                break;
            }
            default:{
                throw new GlobleException(MsgEnum.TYPE_NOTFOUND_ERROR,"无效类型listType："+listType);
            }
        }
        listMethod = ProductService.class.getMethod(methodName, Integer.class, ProductListForm.class);
        return this;
    }

    public Object invoke(Object... params) throws InvocationTargetException, IllegalAccessException {
        return listMethod.invoke(productService,params);
    }

    public ProductService getService(){
        return productService;
    }
}
