package com.xm.api_user;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.xm.api_user.prop.WechatProp;
import me.chanjar.weixin.common.error.WxErrorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ApiUserApplicationTests {

    @Test
    public void contextLoads() throws WxErrorException {
        WechatProp wechatProp = new WechatProp();
        wechatProp.setAppId("wx77c95d4a2f1bfab0");
        wechatProp.setSecret("acecbd89a9dd3009a80052a0905f5b52");
        wechatProp.setToken("GiOleLZJjps03G4dLq3Ogj0ooT2d3DKI");
        wechatProp.setEncodingAESKey("y45Ljp45d5DQIt42TzL0iJPZePDoeIjd5jHoTQEQLe2");



        WxMaDefaultConfigImpl wxMpDefaultConfigImpl = new WxMaDefaultConfigImpl();
        wxMpDefaultConfigImpl.setAppid(wechatProp.getAppId());
        wxMpDefaultConfigImpl.setSecret(wechatProp.getSecret());
        wxMpDefaultConfigImpl.setToken(wechatProp.getToken());
        wxMpDefaultConfigImpl.setAesKey(wechatProp.getEncodingAESKey());

        WxMaService wxService = new WxMaServiceImpl();
        wxService.setWxMaConfig(wxMpDefaultConfigImpl);

        System.out.println(wxService.getUserService().getSessionInfo("023OEloj1Fubuu0iuEmj1Cq6oj1OElol"));
    }

}
