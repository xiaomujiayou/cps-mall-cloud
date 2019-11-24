package com.xm.comment_utils.enu;

import org.springframework.boot.Banner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EnumUtils {

    public static <T extends Enum> T getEnum (Class<T> clzz, String keyName,Object key) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object[] objects = clzz.getEnumConstants();
        String methodName = "get" + keyName.substring(0,1).toUpperCase()+keyName.substring(1,keyName.length());
        Method coinAddressCode = clzz.getMethod(methodName);
        for (Object object : objects) {
            if(coinAddressCode.invoke(object).equals(key)){
                return (T)object;
            }
        }
        throw new EnumConstantNotPresentException(clzz,methodName  +" 找不到所对应的枚举" +" "+ key);
    }

}
