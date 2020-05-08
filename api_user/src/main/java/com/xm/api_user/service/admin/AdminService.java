package com.xm.api_user.service.admin;

import com.xm.comment_serialize.module.mall.form.ListForm;
import com.xm.comment_serialize.module.user.entity.SuAdminEntity;
import com.xm.comment_serialize.module.user.form.AdminAddForm;
import com.xm.comment_serialize.module.user.form.AdminLoginForm;
import com.xm.comment_utils.mybatis.PageBean;

public interface AdminService {

    /**
     * 添加一个管理员
     * @param adminAddForm
     */
    public void add(AdminAddForm adminAddForm);

    /**
     * 管理员登录
     */
    public SuAdminEntity login(AdminLoginForm adminLoginForm);

    /**
     * 删除管理员
     */
    public void del(String userName);

    /**
     * 查询管理员
     */
    public PageBean<SuAdminEntity> get(ListForm listForm);
}
