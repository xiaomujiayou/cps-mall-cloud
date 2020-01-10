package com.xm.api_mall.controller;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.xm.comment.response.Msg;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.mall.form.GetAppShareForm;
import com.xm.comment_utils.img.ImgUtils;
import lombok.Data;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/app")
public class AppController {

}
