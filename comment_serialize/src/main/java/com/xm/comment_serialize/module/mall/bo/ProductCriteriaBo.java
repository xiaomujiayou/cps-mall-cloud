package com.xm.comment_serialize.module.mall.bo;

import com.xm.comment_serialize.module.mall.constant.PlatformTypeConstant;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ProductCriteriaBo {
    private Integer userId;
    private Integer pageNum;
    private Integer pageSize;

    private String pid;
    private String optionId;
    private String keyword;
    private Integer orderBy;
    private Integer minPrice;
    private Integer maxPrice;
    private List<Long> goodsIdList;
    private List<Integer> activityTags;
    private Boolean hasCoupon;

    //是否包邮
    private Boolean parcels;
    //是否为天猫
    private Boolean isTmall;
    //发货地
    private String location;
    //ip 计算邮费
    private String ip;

    public Object getOrderBy(Integer platformType) {
        Map<Integer,Object> pdd = new HashMap<>();
        pdd.put(0,0);
        pdd.put(1,1);
        pdd.put(2,2);
        pdd.put(3,3);
        pdd.put(4,4);
        pdd.put(5,5);
        pdd.put(6,6);
        pdd.put(7,7);
        pdd.put(8,8);
        pdd.put(9,9);
        pdd.put(10,10);
        pdd.put(13,13);
        pdd.put(14,14);

        Map<Integer,Integer> mgj = new HashMap<>();
        mgj.put(0,0);
        mgj.put(1,11);
        mgj.put(2,12);
        mgj.put(3,21);
        mgj.put(4,22);
//        mgj.put(5,31);
        mgj.put(6,32);
        mgj.put(7,41);
        mgj.put(8,42);
//        mgj.put(9,42);
//        mgj.put(10,42);
//        pdd.put(13,13);
//        pdd.put(14,14);

        Map<Integer,Object> tb = new HashMap<>();



        switch (platformType){
            case PlatformTypeConstant.PDD:
                return pdd.get(orderBy);
            case PlatformTypeConstant.MGJ:
                return mgj.get(orderBy);
            case PlatformTypeConstant.TB:
                return tb.get(orderBy);
//            case PlatformTypeConstant.MGJ:
//                return mgj.get(orderBy);

        }
        return orderBy;
    }
}
