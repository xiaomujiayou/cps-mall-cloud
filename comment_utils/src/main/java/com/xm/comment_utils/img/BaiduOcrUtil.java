package com.xm.comment_utils.img;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.ocr.AipOcr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BaiduOcrUtil {

    private static AipOcr aipOcr = new AipOcr("16231910", "vgIRr9rvYHeLusuOdYiK75KZ", "DGzKbzidhrlYa4YAReRx85eKO4zGAZM3");

    public static List<String> img2Str(byte[] img){
        List<String> result = new ArrayList<>();
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("language_type", "CHN_ENG");
        options.put("detect_direction", "false");
        options.put("detect_language", "false");
        options.put("probability", "false");
        String resultStr = aipOcr.basicGeneral(img,options).toString();
        JSONArray resultArr = JSON.parseObject(resultStr).getJSONArray("words_result");
        if(resultArr == null){
            return new ArrayList<>();
        }
        return resultArr.stream().map(new Function<Object, String>() {
            @Override
            public String apply(Object object) {
                return ((JSONObject)object).getString("words");
            }
        }).collect(Collectors.toList());
    }
    
}
