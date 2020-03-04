package com.xm.comment_utils.product;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.Date;

public class GenNumUtil {

    /**
     * 生成订单号
     * @return
     */
    public static String genOrderNum(){
        return DateUtil.format(new Date(),"yyyyMMddHHmmss")+ RandomUtil.randomNumbers(3);
    }

    /**
     * 生成账单号
     * @return
     */
    public static String genBillNum(){
        return DateUtil.format(new Date(),"yyyyMMddHHmmss")+ RandomUtil.randomNumbers(5);
    }
    
    public static void main(String[] args){
        System.out.println(genBillNum());
    }
}
