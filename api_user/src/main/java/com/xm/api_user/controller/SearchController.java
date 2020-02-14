package com.xm.api_user.controller;

import com.xm.api_user.service.SearchService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment_feign.module.mall.feign.MallFeignClient;
import com.xm.comment_serialize.module.mall.constant.ConfigEnmu;
import com.xm.comment_serialize.module.mall.constant.ConfigTypeConstant;
import com.xm.comment_serialize.module.user.entity.SuSearchEntity;
import com.xm.comment_serialize.module.user.form.AddSearchForm;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;
    @Autowired
    private MallFeignClient mallFeignClient;


    @GetMapping
    public String[] getSearch(@LoginUser Integer userId,Integer pageNum,Integer pageSize){
        PageBean<SuSearchEntity> result = searchService.get(userId,pageNum,pageSize);
        return result.getList().stream().map(SuSearchEntity::getKeyword).collect(Collectors.toList()).toArray(new String[0]);
    }

    @DeleteMapping
    public void deleteAll(@LoginUser Integer userId){
        searchService.deleteAll(userId);
    }

    @PostMapping
    public void add(@LoginUser Integer userId, @RequestBody @Valid AddSearchForm addSearchForm, BindingResult bindingResult){
        searchService.add(userId,addSearchForm.getKeyWords());
    }

    @GetMapping("/recommend")
    public String[] getRecommend(@LoginUser(necessary = false) Integer userId, Integer pageNum, Integer pageSize){
        String suggestStr = mallFeignClient.getOneConfig(userId, ConfigEnmu.PRODUCT_SEARCH_RECOMMEND.getName(),ConfigTypeConstant.SYS_CONFIG).getVal();
        return suggestStr.split(",");
    }
}
