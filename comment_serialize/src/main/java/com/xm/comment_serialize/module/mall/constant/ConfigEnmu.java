package com.xm.comment_serialize.module.mall.constant;

public enum ConfigEnmu {
    PDD_PRODUCT_BEST_LIST_SORT("pdd_product_best_list_sort","拼多多推荐商品排序"),
    PDD_PRODUCT_BEST_LIST_SHOP_TYPE("pdd_product_best_list_shop_type","拼多多推荐商品列表店铺类型"),
    MAIN_OPTION_SORT_MAN("main_option_sort_man","主option排序(男性)"),
    MAIN_OPTION_SORT_WOMAN("main_option_sort_woman","主option排序(女性)"),
    MAIN_OPTION_SORT("main_option_sort","主option默认排序"),
    PRODUCT_SHARE_USER_RATE("product_share_user_rate","商品分享者默认佣金比例(千分比)"),
    PRODUCT_SHARE_BUY_RATE("product_share_buy_rate","购买分享商品的用户佣金比例(千分比)"),
    PROXY_LEVEL("proxy_level","代理层级")
    ;
    ConfigEnmu(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    private String desc;
    private String name;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
