package cn.asany.weixin.framework.core;

import cn.asany.weixin.framework.exception.WeixinException;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;

public class CpJsapi implements Jsapi {

  private WxCpService weixinService;

  CpJsapi(final WxCpService weixinService) {
    this.weixinService = weixinService;
  }

  @Override
  public String getTicket() throws WeixinException {
    try {
      return weixinService.getJsapiTicket();
    } catch (WxErrorException e) {
      throw new WeixinException(e.getMessage(), e);
    }
  }

  @Override
  public String getTicket(boolean forceRefresh) throws WeixinException {
    try {
      return weixinService.getJsapiTicket(forceRefresh);
    } catch (WxErrorException e) {
      throw new WeixinException(e.getMessage(), e);
    }
  }

  @Override
  public Signature signature(String url) throws WeixinException {
    try {
      WxJsapiSignature wxJsapiSignature = weixinService.createJsapiSignature(url);
      return new Jsapi.Signature(
          wxJsapiSignature.getNoncestr(),
          wxJsapiSignature.getAppid(),
          wxJsapiSignature.getTimestamp(),
          wxJsapiSignature.getUrl(),
          wxJsapiSignature.getSignature());
    } catch (WxErrorException e) {
      throw new WeixinException(e.getMessage(), e);
    }
  }
}
