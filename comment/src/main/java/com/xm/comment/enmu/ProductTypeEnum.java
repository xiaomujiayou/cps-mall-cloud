package com.xm.comment.enmu;

public enum ProductTypeEnum {
    PRODUCT_ALIBABA(1);

    private Integer productType;

    ProductTypeEnum(Integer productType) {
        this.productType = productType;
    }

    public Integer getProductType() {
        return productType;
    }
}
