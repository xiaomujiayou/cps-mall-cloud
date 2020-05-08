package com.xm.comment_feign.module.user.feign;

import com.xm.comment_feign.module.user.feign.fallback.UserFeignClientFallBack;
import com.xm.comment_serialize.module.lottery.ex.SlPropSpecEx;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import com.xm.comment_serialize.module.user.dto.BillOrderDto;
import com.xm.comment_serialize.module.user.dto.OrderBillDto;
import com.xm.comment_serialize.module.user.entity.SuConfigEntity;
import com.xm.comment_serialize.module.user.entity.SuPidEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.ex.RolePermissionEx;
import com.xm.comment_serialize.module.user.form.AddSearchForm;
import com.xm.comment_serialize.module.user.form.AdminLoginForm;
import com.xm.comment_serialize.module.user.form.GetUserInfoForm;
import com.xm.comment_serialize.module.user.form.WechatLoginForm;
import com.xm.comment_serialize.module.user.vo.MenuTipVo;
import com.xm.comment_serialize.module.user.vo.SuAdminVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "api-user",fallback = UserFeignClientFallBack.class)
public interface UserFeignClient {

    @PostMapping(value = "/user/info",consumes = "application/json")
    public SuUserEntity getUserInfo(@RequestBody GetUserInfoForm getUserInfoForm);

    @GetMapping(value = "/user/info/detail")
    public SuUserEntity getInfoDetail(Integer userId);

    @GetMapping("/role")
    public List<RolePermissionEx> role(Integer userId);

    @GetMapping("/config/all")
    public List<SuConfigEntity> getAllConfig(Integer userId);

    @GetMapping("/config/one")
    public SuConfigEntity getOneConfig(@RequestParam Integer userId, @RequestParam String key);

    @GetMapping("/pid/generate")
    public SuPidEntity generatePid();

    @GetMapping("/pid")
    public SuPidEntity getPid(@RequestParam Integer id);

    @GetMapping("/user/superUser")
    public SuUserEntity superUser(@RequestParam Integer userId,@RequestParam int userType);

    @PostMapping("/product/history")
    public void addProductHistory(@RequestParam Integer userId,@RequestParam Integer platformType,@RequestParam String goodsId);

    @PostMapping(value = "/search",consumes = "application/json")
    public void addSearch(@RequestParam Integer userId,@RequestBody AddSearchForm addSearchForm);

    @PostMapping(value = "/bill/create/prop" ,consumes = "application/json")
    public SuBillToPayBo createByProp(@RequestBody SlPropSpecEx slPropSpecEx);

    @GetMapping("/bill/info")
    public List<BillOrderDto> getBillInfo(@RequestParam Integer userId, @RequestParam List<String> billIds);

    @PostMapping(value = "/menu/tips/num",consumes = "application/json")
    public void addNum(@RequestParam Integer userId,@RequestBody List<Integer> menuIds);

    @PostMapping(value = "/menu/tips/point",consumes = "application/json")
    public void addRedPoint(@RequestParam Integer userId,@RequestBody List<Integer> menuIds);

    @PostMapping(value = "/menu/tips/del",consumes = "application/json")
    public void del(@RequestParam Integer userId,@RequestBody List<Integer> menuIds);

    @GetMapping("/menu/tips")
    public List<MenuTipVo> get(@RequestParam Integer userId, @RequestParam List<Integer> menuIds);

    @PostMapping(value = "/manage/info",consumes = "application/json")
    public SuAdminVo adminInfo(@RequestBody AdminLoginForm adminLoginForm);
}
