package com.xm.comment.response;

/**
 * 错误代码
 */
public enum  MsgEnum {

    SUCESS(200,"sucess"),
    UNKNOWN_ERROR(-1,"未知错误"),
    /* 权限系列（100100） */
    SPIDER_CLIENT_AUTH_ERROR(100101,"爬虫客户端校验失败"),
    SYSTEM_LOGIN_ERROR(100102,"账户或密码错误"),
    SYSTEM_TOKEN_ERROR(100103,"无效token"),
    SYSTEM_APPSECRET_ERROR(100103,"无效AppSecret"),
    SYSTEM_INVALID_USER_ERROR(100104,"无效用户"),
    SYSTEM_AUTH_ERROR(100105,"没有权限"),
    SYSTEM_AUTH_EXPIRE_ERROR(100106,"账户已过期"),


    /*错误系列（100200）*/


    /* 数据系列（100300）*/
    PARAM_VALID_ERROR(10301,"参数校验失败"),
    NO_DATA_ERROR(10302,"无可用数据"),
    DATA_ALREADY_EXISTS(10303,"数据已存在"),
    DATA_ALREADY_EXISTS_OR_CONTAIN(10304,"数据已存在,或已经包含"),
    DATA_ALREADY_NOT_EXISTS(10305,"数据不存在"),
    DATA_INVALID_ERROR(10306,"无效数据"),
    TYPE_NOTFOUND_ERROR(10307,"找不到指定类型"),

    /* 服务系列*/
    SERVICE_AVAILABLE(10401,"服务不可用"),

    /* 用户系列 */
    USER_NOFOUND_ERROR(100501,"用户不存在"),
    USER_ID_ERROR(100502,"无效用户id")
    ;
    private Integer code;
    private String msg;

    MsgEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
