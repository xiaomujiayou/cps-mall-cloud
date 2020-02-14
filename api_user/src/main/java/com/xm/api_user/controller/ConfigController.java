package com.xm.api_user.controller;

import com.xm.api_user.mapper.SuConfigMapper;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.R;
import com.xm.comment_serialize.module.user.entity.SuConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private SuConfigMapper suConfigMapper;

    @GetMapping("/all")
    public List<SuConfigEntity> getAllConfig(Integer userId){
        Example example = new Example(SuConfigEntity.class);
        example.createCriteria().andEqualTo("userId",userId);
        return suConfigMapper.selectByExample(example);
    }
    @GetMapping("/one")
    public SuConfigEntity getOneConfig(Integer userId, String name){
        Example example = new Example(SuConfigEntity.class);
        example.createCriteria()
                .andEqualTo("userId",userId)
                .andEqualTo("name",name);
        return suConfigMapper.selectOneByExample(example);
    }
}
