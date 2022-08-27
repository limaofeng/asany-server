package cn.asany.security.auth.utils;

import javax.servlet.http.HttpServletRequest;
import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.oauth2.core.OAuth2Authentication;
import org.jfantasy.framework.security.oauth2.core.OAuth2AuthenticationDetails;
import org.jfantasy.framework.security.oauth2.core.TokenType;

public class AuthUtils {

  public static OAuth2Authentication buildOAuth2(
      String clientId, HttpServletRequest request, Authentication authentication) {
    OAuth2AuthenticationDetails oAuth2AuthenticationDetails = new OAuth2AuthenticationDetails();
    oAuth2AuthenticationDetails.setClientId(clientId);
    oAuth2AuthenticationDetails.setTokenType(TokenType.SESSION);
    oAuth2AuthenticationDetails.setRequest(request);
    return new OAuth2Authentication(authentication, oAuth2AuthenticationDetails);
  }
}
