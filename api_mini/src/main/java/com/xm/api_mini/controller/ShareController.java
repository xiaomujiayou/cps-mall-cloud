package com.xm.api_mini.controller;

import cn.hutool.core.img.ImgUtil;
import com.xm.api_mini.poster.GoodsPoster;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping("/share")
@Controller
public class ShareController {

    @GetMapping(value = "/goods",produces = MediaType.IMAGE_PNG_VALUE)
    public void goods(SmProductEntityEx smProductEntityEx, HttpServletResponse res) throws IOException {
        ImgUtil.writePng(new GoodsPoster(smProductEntityEx).draw(),res.getOutputStream());
    }

}
