package com.xm.api_user.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.xm.api_user.service.admin.AdminService;
import com.xm.comment_serialize.module.user.entity.SuAdminEntity;
import com.xm.comment_serialize.module.user.form.AdminAddForm;
import com.xm.comment_serialize.module.user.form.AdminLoginForm;
import com.xm.comment_serialize.module.user.vo.SuAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/manage")
public class ManageController {

    @Autowired
    private AdminService adminService;

    /**
     * 添加管理员
     */
    @PostMapping
    public void add(@Valid @RequestBody AdminAddForm adminAddForm, BindingResult bindingResult){
        adminService.add(adminAddForm);
    }

    /**
     * 登录
     * @return
     */
    @PostMapping("/info")
    public SuAdminVo info(@RequestBody AdminLoginForm adminLoginForm){
        SuAdminEntity suAdminEntity = adminService.login(adminLoginForm);
        SuAdminVo suAdminVo = new SuAdminVo();
        BeanUtil.copyProperties(suAdminEntity,suAdminVo);
        return suAdminVo;
    }
}
