package com.xm.api_mall.exception;

public class MallApiResponseException extends RuntimeException {
    private String errorCode;
    private String errorMsg;

    public MallApiResponseException(String errorCode, String errorMsg) {
        super(String.format("pdd api 异常 code: %s; msg: %s",errorCode,errorMsg));
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
