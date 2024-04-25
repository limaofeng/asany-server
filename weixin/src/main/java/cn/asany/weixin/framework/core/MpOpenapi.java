/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.weixin.framework.core;

import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.message.user.User;
import cn.asany.weixin.framework.oauth2.AccessToken;
import cn.asany.weixin.framework.oauth2.Scope;
import cn.asany.weixin.framework.util.WeixinUtil;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.http.URIUtil;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import net.asany.jfantasy.framework.util.common.StringUtil;

public class MpOpenapi implements Openapi {

  private WxMpService wxMpService;

  MpOpenapi(WxMpService wxMpService) {
    this.wxMpService = wxMpService;
  }

  @Override
  public AccessToken getAccessToken(String code) throws WeixinException {
    try {
      WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
      return new AccessToken(
          wxMpOAuth2AccessToken.getAccessToken(),
          wxMpOAuth2AccessToken.getExpiresIn(),
          wxMpOAuth2AccessToken.getRefreshToken(),
          wxMpOAuth2AccessToken.getOpenId(),
          wxMpOAuth2AccessToken.getScope());
    } catch (WxErrorException e) {
      throw new WeixinException(e.getMessage(), e);
    }
  }

  @Override
  public User getUser(String code) throws WeixinException {
    AccessToken accessToken = getAccessToken(code);
    if (accessToken == null) {
      throw new WeixinException(code + " ==> AccessToken is null ");
    }
    return getUser(accessToken);
  }

  @Override
  public User getUser(AccessToken token) throws WeixinException {
    try {
      if (Scope.userinfo == token.getScope()) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        wxMpOAuth2AccessToken.setAccessToken(token.getToken());
        wxMpOAuth2AccessToken.setExpiresIn(token.getExpiresIn());
        wxMpOAuth2AccessToken.setOpenId(token.getOpenId());
        wxMpOAuth2AccessToken.setRefreshToken(token.getRefreshToken());
        wxMpOAuth2AccessToken.setScope(token.getScope().name());
        return WeixinUtil.toUser(wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN"));
      } else {
        return WeixinUtil.toUser(wxMpService.getUserService().userInfo(token.getOpenId(), null));
      }
    } catch (WxErrorException e) {
      throw new WeixinException(e.getMessage(), e);
    }
  }

  @Override
  public String getAuthorizationUrl(String redirectUri, Scope scope, String state)
      throws WeixinException {
    String url = "https://open.weixin.qq.com/connect/oauth2/authorize?";
    url += "appid=" + wxMpService.getWxMpConfigStorage().getAppId();
    url += "&redirect_uri=" + URIUtil.encodeURIComponent(redirectUri);
    url += "&response_type=code";
    url += "&scope=" + scope.getValue();
    if (StringUtil.isNotBlank(state)) {
      url += "&state=" + state;
    }
    url += "#wechat_redirect";
    return url;
  }
}
