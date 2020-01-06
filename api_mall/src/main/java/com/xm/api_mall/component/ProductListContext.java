package com.xm.api_mall.component;

import com.xm.api_mall.service.ProductService;
import com.xm.comment.exception.GlobleException;
import com.xm.comment.response.MsgEnum;
import com.xm.comment_serialize.module.mall.constant.ProductListTypeConstant;
import com.xm.comment_serialize.module.mall.constant.ProductListTypeEnum;
import com.xm.comment_serialize.module.mall.form.ProductListForm;
import com.xm.comment_utils.enu.EnumUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ProductListContext<T> {

    private ProductService productService;
    private Method listMethod;

    public ProductListContext(ProductService productService) {
        this.productService = productService;
    }

    public ProductListContext listType(Integer listType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String methodName = EnumUtils.getEnum(ProductListTypeEnum.class,"key",listType).getName();
        listMethod = ProductService.class.getMethod(methodName, Integer.class,String.class, ProductListForm.class);
        return this;
    }

    public Object invoke(Object... params) throws InvocationTargetException, IllegalAccessException {
        return listMethod.invoke(productService,params);
    }

    public ProductService getService(){
        return productService;
    }
}
