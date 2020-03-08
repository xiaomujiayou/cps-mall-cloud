package com.xm.comment_serialize.module.user.vo;

import lombok.Data;

@Data
public class UserProfitVo {
    public UserProfitVo() {}
    public UserProfitVo(String name, String value, String url) {
        this.name = name;
        this.value = value;
        this.url = url;
    }

    private String name;
    private String value;
    private String url;
}
