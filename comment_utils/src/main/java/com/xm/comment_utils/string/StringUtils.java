package com.xm.comment_utils.string;

public class StringUtils {

    /**
     * 取出中间文本
     * @param before
     * @param after
     * @return
     */
    public static String getBetweenStr(String text,String before,String after){
        Integer start = text.indexOf(before);
        if(start < 0)
            throw new RuntimeException("找不到前文本");
        start += before.length();
        Integer end = text.indexOf(after,start);
        if(end < 0)
            throw new RuntimeException("找不到后文本");
        return text.substring(start,end);
    }

    /**
     * 收缩文本长度到指定范围
     * @param targetStr
     * @param length
     * @return
     */
    public static String shrinkText(String targetStr,Integer length){
        if(targetStr == null || targetStr.length() <= length){
            return targetStr;
        }
        targetStr = targetStr.replaceAll("[【】\\（\\）\\《\\》\\——\\；\\，\\。\\“\\”\\<\\>]","");
        if(targetStr.length() <= length){
            return  targetStr;
        }
        targetStr = targetStr.replaceAll("[\\[\\]\\(\\)\\<\\>\\-\\;\\,\\.\\{\\}\\*\\$\\#]","");
        if(targetStr.length() <= length){
            return  targetStr;
        }
        targetStr = targetStr.replaceAll("\\s","");
        if(targetStr.length() <= length){
            return  targetStr;
        }
        return targetStr.substring(0,length);
    }

    public static boolean isNumeric(String str){
        for(int i=str.length();--i>=0;){
            int chr=str.charAt(i);
            if(chr<48 || chr>57)
                return false;
        }
        return true;
    }




    public static void main(String[] args){
        System.out.println(isNumeric("56456"));
        System.out.println(isNumeric("66"));
        System.out.println(isNumeric("66a"));

    }
}
