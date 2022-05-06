package cn.asany.weixin.framework.core;

import cn.asany.weixin.framework.session.WeixinApp;
import me.chanjar.weixin.cp.api.WxCpInMemoryConfigStorage;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.WxCpServiceImpl;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;

public class WeixinDetails {
  private WeixinService weixinService;

  public WeixinDetails(WeixinApp weixinApp) {
    if (WeixinApp.Type.enterprise == weixinApp.getType()) {
      WxCpService wxCpService = new WxCpServiceImpl();
      WxCpInMemoryConfigStorage wxMpConfigStorage = new WxCpInMemoryConfigStorage();
      wxMpConfigStorage.setCorpId(weixinApp.getId());
      wxMpConfigStorage.setCorpSecret(weixinApp.getSecret());
      wxMpConfigStorage.setAgentId(weixinApp.getAgentId());
      wxMpConfigStorage.setToken(weixinApp.getToken());
      wxMpConfigStorage.setAesKey(weixinApp.getAesKey());
      wxCpService.setWxCpConfigStorage(wxMpConfigStorage);
      weixinService = new WeixinCpService(wxCpService, wxMpConfigStorage);
    } else {
      WxMpService wxMpService = new WxMpServiceImpl();
      WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage();
      wxMpConfigStorage.setAppId(weixinApp.getId());
      wxMpConfigStorage.setSecret(weixinApp.getSecret());
      wxMpConfigStorage.setToken(weixinApp.getToken());
      wxMpConfigStorage.setAesKey(weixinApp.getAesKey());
      wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
      weixinService = new WeixinMpService(wxMpService, wxMpConfigStorage);
    }
  }

  public WeixinService getWeixinService() {
    return weixinService;
  }
}
