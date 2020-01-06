package com.xm.api_mall.utils;

import com.xm.comment_serialize.module.mall.constant.PlatformTypeConstant;
import com.xm.comment_serialize.module.mall.vo.SmProductSimpleVo;
import com.xm.comment_serialize.module.mall.vo.SmProductVo;
import com.xm.comment_utils.rege.RegeUtils;
import lombok.Data;

import java.util.List;
import java.util.regex.Pattern;

public class TextToGoodsUtils {
    public static Pattern PDD_URL_PATTERN = Pattern.compile("(?<=(yangkeduo)\\S{1,255}(goods_id=))[0-9]+");

    /**
     * 解析剪辑版内容
     * @param text
     */
    public static GoodsSpec parse(String text){
        GoodsSpec goodsSpec = new GoodsSpec();
        String goodsId = RegeUtils.matchFrist(text,PDD_URL_PATTERN);
        if(goodsId!=null){
            goodsSpec.setGoodsId(goodsId);
            goodsSpec.setPlatformType(PlatformTypeConstant.PDD);
            goodsSpec.setParseType(1);
            return goodsSpec;
        }
        goodsSpec.setParseType(0);
        return goodsSpec;
    }

    @Data
    public static class GoodsSpec{
        private Integer platformType;
        private String goodsId;
        //解析结果类型:0:解析失败,1:解析为精确商品,2:商品简要信息,3:解析为商品名称查询
        private Integer parseType;
        private SmProductVo goodsInfo;
        private SmProductSimpleVo simpleInfo;
    }
}
