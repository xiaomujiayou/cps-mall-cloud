package com.xm.comment_serialize.module.mall.ex;

import cn.hutool.core.clone.Cloneable;
import lombok.Data;

@Data
public class ActiveInfo implements Cloneable {
    private Integer activeId;
    private Integer money;
    private String name;
    private String des;

    @Override
    public ActiveInfo clone() {
        System.out.println("11111111111111");
        ActiveInfo info = new ActiveInfo();
        info.setActiveId(activeId);
        info.setMoney(money);
        info.setName(name);
        info.setDes(des);
        return info;
    }
}
