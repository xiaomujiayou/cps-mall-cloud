package com.xm.wind_control.controller.manage;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.xm.comment_serialize.module.wind.entity.SwApiRecordEntity;
import com.xm.comment_serialize.module.wind.form.DelayForm;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.wind_control.mapper.SwApiRecordMapper;
import com.xm.wind_control.mapper.custom.SwApiRecordMapperEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.List;

@RestController
@RequestMapping("/manage/delay")
public class DelayController {

    @Autowired
    private SwApiRecordMapper swApiRecordMapper;
    @Autowired
    private SwApiRecordMapperEx swApiRecordMapperEx;


    /**
     * 查询API记录
     */
    @GetMapping
    public PageBean<SwApiRecordEntity> getApi(DelayForm form) {
        PageHelper.startPage(form.getPageNum(), form.getPageSize());
        OrderByHelper.orderBy("create_time desc");
        List<SwApiRecordEntity> list = swApiRecordMapper.selectByExample(formToExample(form));
        PageBean<SwApiRecordEntity> result = new PageBean<>(list);
        return result;
    }

    /**
     * API记录统计
     */
    @GetMapping("/count")
    public Integer getApiCount(DelayForm form) {
        return swApiRecordMapper.selectCountByExample(formToExample(form));
    }

    /**
     * API平均延时统计
     */
    @GetMapping("/average")
    public Integer getApiAverage(DelayForm form) {
        Integer result = swApiRecordMapperEx.getApiAverage(form);
        return result == null ? 0 : result;
    }

    private Example formToExample(DelayForm form) {
        Example example = new Example(SwApiRecordEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if (StrUtil.isNotBlank(form.getUrl()))
            criteria.andEqualTo("url", form.getUrl());
        if (StrUtil.isNotBlank(form.getMethod()))
            criteria.andEqualTo("method", form.getMethod());
        if (ObjectUtil.isAllNotEmpty(form.getCreateStart(), form.getCreateEnd()))
            criteria.andBetween("createTime", form.getCreateStart(), form.getCreateEnd());
        return example;
    }
}
