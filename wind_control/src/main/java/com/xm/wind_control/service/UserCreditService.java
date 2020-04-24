package com.xm.wind_control.service;


import com.xm.comment_serialize.module.wind.constant.ChangeCreditEnum;

public interface UserCreditService {

    /**
     * 用户信用变动
     */
    public void changeCredit(Integer userId,ChangeCreditEnum changeCreditEnum,String attached);
}
