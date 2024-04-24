package cn.asany.security.auth.utils;

import jakarta.servlet.http.HttpServletRequest;
import net.asany.jfantasy.framework.security.auth.TokenType;
import net.asany.jfantasy.framework.security.auth.oauth2.core.OAuth2Authentication;
import net.asany.jfantasy.framework.security.auth.oauth2.core.OAuth2AuthenticationDetails;
import net.asany.jfantasy.framework.security.authentication.Authentication;

public class AuthUtils {

  public static OAuth2Authentication buildOAuth2(
      String clientId, HttpServletRequest request, Authentication authentication) {
    OAuth2AuthenticationDetails oAuth2AuthenticationDetails = new OAuth2AuthenticationDetails();
    oAuth2AuthenticationDetails.setClientId(clientId);
    oAuth2AuthenticationDetails.setTokenType(TokenType.SESSION_ID);
    oAuth2AuthenticationDetails.setRequest(request);
    return new OAuth2Authentication(authentication, oAuth2AuthenticationDetails);
  }
}
