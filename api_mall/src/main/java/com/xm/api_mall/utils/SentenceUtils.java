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

        String str = "恒源祥男袜子秋季薄款运动棉袜透气短筒防臭吸汗低帮浅口隐形袜子";
//        List<Term> phraseList = HanLP.segment(str);
        System.out.println(getUpset(str));
    }
}
