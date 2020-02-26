package com.xm.api_mall.controller;

import cn.hutool.core.bean.BeanUtil;
import com.xm.api_mall.component.PlatformContext;
import com.xm.api_mall.service.BannerService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment_feign.module.user.feign.UserFeignClient;
import com.xm.comment_serialize.module.mall.vo.MenuVo;
import com.xm.comment_serialize.module.user.vo.MenuTipVo;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_utils.response.R;
import com.xm.comment_serialize.module.mall.constant.BannerTypeEnum;
import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import com.xm.comment_utils.enu.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;
    @Autowired
    private PlatformContext productContext;
    @Autowired
    private UserFeignClient userFeignClient;

    /**
     * 获取banner
     * @param type
     * @return
     * @throws Exception
     */
    @GetMapping("/{type}")
    public List<MenuVo> banner(@LoginUser(necessary = false) Integer userId, @PathVariable("type") Integer type) throws Exception {
        List<SmBannerEntity> smBannerEntities = bannerService.getBannerByType(EnumUtils.getEnum(BannerTypeEnum.class,"type",type));
        List<MenuVo> result = null;
        if(userId != null){
            List<Integer> ids = smBannerEntities.stream().map(SmBannerEntity::getId).collect(Collectors.toList());
            List<MenuTipVo> tips = userFeignClient.get(userId,ids);
            result = smBannerEntities.stream().map(o->{
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
            result = smBannerEntities.stream().map(o->{
                MenuVo menuVo = new MenuVo();
                BeanUtil.copyProperties(o,menuVo);
                return menuVo;
            }).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 获取主题活动列表
     * @param platformType
     * @return
     * @throws Exception
     */
    @GetMapping("/theme")
    public List<SmBannerEntity> themeBanner(Integer platformType) throws Exception {
        if(platformType == null)
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR);
        return productContext
                .platformType(platformType)
                .getService()
                .themes();
    }
}
