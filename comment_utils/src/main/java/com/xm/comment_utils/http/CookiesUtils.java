package com.xm.comment_utils.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookiesUtils {
    public static Cookie getCookie(HttpServletRequest request,String cookieName){
        Cookie[] cookies = request.getCookies();
        if(cookies == null)
            return null;
        for (Cookie cookie:cookies){
            if(cookie.getName().trim().equals(cookieName))
                return cookie;
        }
        return null;
    }
}
