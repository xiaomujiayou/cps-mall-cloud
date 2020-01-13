package com.xm.comment_utils.exception;

import com.xm.comment_utils.response.MsgEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义全局异常
 */
public class GlobleException extends RuntimeException{

    @Getter
    @Setter
    public MsgEnum msgEnum;

    public GlobleException(MsgEnum msgEnum) {
        super(msgEnum.getMsg());
        this.msgEnum = msgEnum;
    }

    public GlobleException(MsgEnum msgEnum,String... stackInfo) {
        super(msgEnum.getMsg() + " " + String.join(" ",stackInfo));
        this.msgEnum = msgEnum;
    }
}
