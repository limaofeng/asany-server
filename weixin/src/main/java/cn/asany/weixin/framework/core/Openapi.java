package cn.asany.weixin.framework.core;

import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.message.user.User;
import cn.asany.weixin.framework.oauth2.AccessToken;
import cn.asany.weixin.framework.oauth2.Scope;

public interface Openapi {

  User getUser(String code) throws WeixinException;

  User getUser(AccessToken token) throws WeixinException;

  AccessToken getAccessToken(String code) throws WeixinException;

  String getAuthorizationUrl(String redirectUri, Scope scope, String state) throws WeixinException;
}
