package com.xm.api_mall.controller;

import cn.hutool.core.bean.BeanUtil;
import com.xm.api_mall.service.MenuService;
import com.xm.api_mall.service.api.BannerService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment.annotation.PlatformType;
import com.xm.comment_feign.module.user.feign.UserFeignClient;
import com.xm.comment_serialize.form.BaseForm;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import com.xm.comment_serialize.module.mall.entity.SmMenuEntity;
import com.xm.comment_serialize.module.mall.form.BannerForm;
import com.xm.comment_serialize.module.mall.vo.MenuVo;
import com.xm.comment_serialize.module.user.vo.MenuTipVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private UserFeignClient userFeignClient;

    /**
     * 获取底部导航菜单
     */
    @GetMapping("/main/tab")
    public List<MenuVo> banner(@LoginUser(necessary = false) @PlatformType BaseForm baseForm) throws Exception {
        List<SmMenuEntity> menuEntities = menuService.mainTabs(baseForm);
        return getUserMenu(baseForm.getUserId(),menuEntities);
    }

    /**
     * 获取设置菜单
     * @param baseForm
     * @return
     * @throws Exception
     */
    @GetMapping("/setting")
    public List<MenuVo> optBanner(@LoginUser(necessary = false) @PlatformType BaseForm baseForm) throws Exception {
        List<SmMenuEntity> menuEntities = menuService.setting(baseForm);
        return getUserMenu(baseForm.getUserId(),menuEntities);
    }

    private List<MenuVo> getUserMenu(Integer userId,List<SmMenuEntity> smMenuEntities){
        List<MenuVo> result = null;
        if(userId != null){
            List<Integer> ids = smMenuEntities.stream().map(SmMenuEntity::getId).collect(Collectors.toList());
            List<MenuTipVo> tips = userFeignClient.get(userId,ids);
            result = smMenuEntities.stream().map(o->{
                MenuVo menuVo = new MenuVo();
                BeanUtil.copyProperties(o,menuVo);
                MenuTipVo target = tips.stream().filter(j->j.getMenuId() == o.getId()).findFirst().orElse(null);
                if(target == null)
                    return menuVo;
                menuVo.setHot(target.getHot());
                menuVo.setNum(target.getNum());
                return menuVo;
            }).collect(Collectors.toList());
        }else {
            result = smMenuEntities.stream().map(o->{
                MenuVo menuVo = new MenuVo();
                BeanUtil.copyProperties(o,menuVo);
                return menuVo;
            }).collect(Collectors.toList());
        }
        return result;
    }
}
