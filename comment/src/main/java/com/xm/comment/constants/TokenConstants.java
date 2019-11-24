package com.xm.comment.constants;

public class TokenConstants {
    //redis token前缀
    public static String REDIS_KEY_PREFIX = "USER_TOKEN";
    //header token名称
    public static String REQUEST_HEADER_NAME = "token";
    //过期时间(会自动续期)
    public static Integer EXPIRE = 24 * 3600;
}
