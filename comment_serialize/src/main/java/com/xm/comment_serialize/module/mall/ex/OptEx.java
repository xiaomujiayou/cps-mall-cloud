package com.xm.comment_serialize.module.mall.ex;

import com.xm.comment_serialize.module.mall.entity.SmOptEntity;
import lombok.Data;

import java.util.List;

@Data
public class OptEx extends SmOptEntity {
    private List<OptEx> childs;
}
