package com.xm.comment_utils.similar;

import com.xm.comment_utils.number.NumberUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @description 比较相似度
 */
public class SimilarUtils {
    //生僻子
    private static List<String> RARE_WORDS = Arrays.asList("熙","铪","喇","唛","膈");
    /**
     * 自动补全长度
     * @param doc1
     * @param doc2
     * @return
     */
    public static double getSimilarityAutoLength(String doc1, String doc2){
        if(doc1 == null || doc2 == null) {
            throw new NullPointerException(" the Document is null or have not cahrs!!");
        }
        if(doc1.length() == doc2.length()){
            return getSimilarity(doc1,doc2);
        }
        String str1 = doc1.length()>doc2.length()?doc2:doc1;
        String str2 = doc1.length()<doc2.length()?doc2:doc1;

        str1 = str1+getRareWord(str2.length() - str1.length());
        return getSimilarity(str1,str2);
    }
    
    public static void main(String[] args){
        System.out.println(getSimilarity("面料主材质","材质"));
        System.out.println(getSimilarityAutoLength("面料主材质","材质"));
    }

    private static String getRareWord(Integer size){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(RARE_WORDS.get(NumberUtils.getRandom(0,RARE_WORDS.size() - 1)));
        }
        return stringBuilder.toString();
    }

    public static double getSimilarity(String doc1, String doc2) {
        if (doc1 != null && doc1.trim().length() > 0 && doc2 != null && doc2.trim().length() > 0) {
            Map<Integer, int[]> AlgorithmMap = new HashMap<Integer, int[]>();
            for (int i = 0; i < doc1.length(); i++) {
                char d1 = doc1.charAt(i);
                if (isHanZi(d1)) {
                    int charIndex = getGB2312Id(d1);
                    if (charIndex != -1) {
                        int[] fq = AlgorithmMap.get(charIndex);
                        if (fq != null && fq.length == 2) {
                            fq[0]++;
                        } else {
                            fq = new int[2];
                            fq[0] = 1;
                            fq[1] = 0;
                            AlgorithmMap.put(charIndex, fq);
                        }
                    }
                }
            }

            for (int i = 0; i < doc2.length(); i++) {
                char d2 = doc2.charAt(i);
                if (isHanZi(d2)) {
                    int charIndex = getGB2312Id(d2);
                    if (charIndex != -1) {
                        int[] fq = AlgorithmMap.get(charIndex);
                        if (fq != null && fq.length == 2) {
                            fq[1]++;
                        } else {
                            fq = new int[2];
                            fq[0] = 0;
                            fq[1] = 1;
                            AlgorithmMap.put(charIndex, fq);
                        }
                    }
                }
            }

            Iterator<Integer> iterator = AlgorithmMap.keySet().iterator();
            double sqdoc1 = 0;
            double sqdoc2 = 0;
            double denominator = 0;
            while (iterator.hasNext()) {
                int[] c = AlgorithmMap.get(iterator.next());
                denominator += c[0] * c[1];
                sqdoc1 += c[0] * c[0];
                sqdoc2 += c[1] * c[1];
            }
            double origin = denominator / Math.sqrt(sqdoc1 * sqdoc2);
            if (String.valueOf(origin).equals("NaN")) {
                return Double.valueOf("0");
            }
            BigDecimal bg = new BigDecimal(origin);
            double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return f1;
        } else {
            throw new NullPointerException(" the Document is null or have not cahrs!!");
        }
    }

    public static boolean isHanZi(char ch) {
        return (ch >= 0x4E00 && ch <= 0x9FA5);
    }

    /**
     * 根据输入的Unicode字符，获取它的GB2312编码或者ascii编码，
     *
     * @param ch 输入的GB2312中文字符或者ASCII字符(128个)
     * @return ch在GB2312中的位置，-1表示该字符不认识
     */
    public static short getGB2312Id(char ch) {
        try {
            byte[] buffer = Character.toString(ch).getBytes("GB2312");
            if (buffer.length != 2) {
// 正常情况下buffer应该是两个字节，否则说明ch不属于GB2312编码，故返回'?'，此时说明不认识该字符
                return -1;
            }
            int b0 = (buffer[0] & 0x0FF) - 161; // 编码从A1开始，因此减去0xA1=161
            int b1 = (buffer[1] & 0x0FF) - 161; // 第一个字符和最后一个字符没有汉字，因此每个区只收16*6-2=94个汉字
            return (short) (b0 * 94 + b1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static int compare(String str, String target) {
        int d[][]; // 矩阵
        int n = str.length();
        int m = target.length();
        int i; // 遍历str的
        int j; // 遍历target的
        char ch1; // str的
        char ch2; // target的
        int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++) { // 初始化第一列
            d[i][0] = i;
        }
        for (j = 0; j <= m; j++) { // 初始化第一行
            d[0][j] = j;
        }
        for (i = 1; i <= n; i++) { // 遍历str
            ch1 = str.charAt(i - 1);
// 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
// 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1]
                        + temp);
            }
        }
        return d[n][m];
    }

    private static int min(int one, int two, int three) {
        return (one = one < two ? one : two) < three ? one : three;
    }

    /**
     * 获取两字符串的相似度
     *
     * @param str
     * @param target
     * @return
     */
    public static float getSimilarityRatio(String str, String target) {
        return 1 - (float) compare(str, target)
                / Math.max(str.length(), target.length());
    }

//    public static void main(String[] args) {
//        //
//        String str1 = " rrr11ttrrr";
//        String str2 = "titl四五六e4";
//        String str3 = "这个杀手 不太冷";
//        String str4 = "杀手 冷";
//        String str5 = "这个 杀手 不";
//
//        System.out.println(getSimilarity("颜色分类", "类别"));
//
//    }

}