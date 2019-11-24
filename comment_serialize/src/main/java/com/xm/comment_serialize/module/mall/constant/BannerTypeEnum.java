package com.xm.comment_serialize.module.mall.constant;

import lombok.Data;

public enum  BannerTypeEnum {
    HOME("首页轮播图",1),
    HOME_SLIDE_MUEN("首页首页滑动菜单",2),
    BOTTOM_NAVIGNTION("底部导航",3);

    private String name;
    private Integer type;

    BannerTypeEnum(String name, Integer type) {
        this.name = name;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
