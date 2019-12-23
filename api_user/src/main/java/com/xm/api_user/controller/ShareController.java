package com.xm.api_user.controller;

import com.xm.api_user.service.SearchService;
import com.xm.api_user.service.ShareService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.exception.GlobleException;
import com.xm.comment.module.mall.feign.MallFeignClient;
import com.xm.comment.response.Msg;
import com.xm.comment.response.MsgEnum;
import com.xm.comment.response.R;
import com.xm.comment_serialize.module.mall.constant.ConfigEnmu;
import com.xm.comment_serialize.module.mall.constant.ConfigTypeConstant;
import com.xm.comment_serialize.module.user.entity.SuSearchEntity;
import com.xm.comment_serialize.module.user.form.AddSearchForm;
import com.xm.comment_serialize.module.user.form.GetUserShareForm;
import com.xm.comment_serialize.module.user.vo.ShareVo;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/share")
public class ShareController {

    @Autowired
    private ShareService shareService;
    @Autowired
    private MallFeignClient mallFeignClient;


    @GetMapping
    public Msg<PageBean<ShareVo>> get(@LoginUser Integer userId, GetUserShareForm getUserShareForm){
        return R.sucess(shareService.getList(
                userId,
                getUserShareForm.getOrderType(),
                getUserShareForm.getOrder(),
                getUserShareForm.getPageNum(),
                getUserShareForm.getPageSize()));

    }

    @DeleteMapping("/{id}")
    public Msg deleteAll(@LoginUser Integer userId,@PathVariable("id")Integer id){
        if(id == null || id == 0)
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR);
        shareService.del(userId,id);
        return R.sucess();
    }
}
