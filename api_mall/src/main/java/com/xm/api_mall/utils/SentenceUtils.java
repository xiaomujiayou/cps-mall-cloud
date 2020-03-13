package com.xm.api_mall.utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 语言处理工具包
 * by hanlp
 */
public class SentenceUtils {
    static {
        try {
            List<String> keywords = IOUtils.readLines(SentenceUtils.class.getResourceAsStream("/dicts/hanlp.dict"),"UTF-8");
            if(keywords != null){
                for(String keyword:keywords) {
                    CustomDictionary.add(keyword);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 分词
     * @param sentence
     * @return
     */
    public static List<String> participles(String sentence){
        return HanLP.segment(sentence).stream().map(new Function<Term, String>() {
            @Override
            public String apply(Term term) {
                return term.word;
            }
        }).collect(Collectors.toList());
    }

    /**
     * 取摘要
     * @param sentence
     * @return
     */
    public static String getAbstract(String sentence){

        return HanLP.extractSummary(sentence, 1).get(0);
    }

    /**
     * 取近似语句
     * @param sentence
     * @return
     */
    public static String getUpset(String sentence){
        StringBuilder stringBuilder = new StringBuilder();
        new HashSet<>(participles(sentence)).forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                stringBuilder.append(s);
            }
        });
        return stringBuilder.toString();
    }
    
    public static void main(String[] args) throws IOException {

//        String str = "【镇宅辟邪】桃木中国结挂件客厅大号福字电视墙过年乔迁对联挂饰";
        String str = "正牌微信支付服务商，已申请下来所有微信支付代理资质";

        List<String> p = SentenceUtils.participles(str);
        double a = (p.stream().filter(o -> o.length() >= 3).count())/Double.valueOf(p.size());
        double b = (p.stream().filter(o -> o.length() == 2).count())/Double.valueOf(p.size());
        System.out.println(p);
        System.out.println(a*3 + b*1.5);
    }
}
