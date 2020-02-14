package com.xm.comment.exception;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.Date;


/**
 * 服务内部异常
 */
@Data
public class InternalApiException extends Exception {
    private Integer status;
    private String message;
    private String path;
    private String error;
    private String timestamp;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
