package cn.asany.security.oauth.service;

import cn.asany.security.oauth.convert.AccessTokenConverter;
import cn.asany.security.oauth.domain.AccessToken;
import cn.asany.security.oauth.vo.PersonalAccessToken;
import java.util.Optional;
import net.asany.jfantasy.framework.error.ValidationException;
import net.asany.jfantasy.framework.security.SecurityContextHolder;
import net.asany.jfantasy.framework.security.auth.TokenType;
import net.asany.jfantasy.framework.security.auth.oauth2.DefaultTokenServices;
import net.asany.jfantasy.framework.security.auth.oauth2.JwtTokenPayload;
import net.asany.jfantasy.framework.security.auth.oauth2.core.OAuth2AccessToken;
import net.asany.jfantasy.framework.security.auth.oauth2.core.OAuth2Authentication;
import net.asany.jfantasy.framework.security.auth.oauth2.core.OAuth2AuthenticationDetails;
import net.asany.jfantasy.framework.security.auth.oauth2.jwt.JwtUtils;
import net.asany.jfantasy.framework.security.authentication.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceUtils {

  private final AccessTokenService accessTokenService;
  private final DefaultTokenServices tokenServices;

  private final AccessTokenConverter accessTokenConverter;

  public TokenServiceUtils(
      DefaultTokenServices tokenServices,
      AccessTokenService accessTokenService,
      AccessTokenConverter accessTokenConverter) {
    this.tokenServices = tokenServices;
    this.accessTokenService = accessTokenService;
    this.accessTokenConverter = accessTokenConverter;
  }

  public PersonalAccessToken createPersonalAccessToken(String clientId, String name) {
    OAuth2AuthenticationDetails oAuth2AuthenticationDetails = new OAuth2AuthenticationDetails();
    oAuth2AuthenticationDetails.setClientId(clientId);
    oAuth2AuthenticationDetails.setTokenType(TokenType.PERSONAL_ACCESS_TOKEN);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    OAuth2Authentication oAuth2Authentication =
        new OAuth2Authentication(authentication, oAuth2AuthenticationDetails);
    oAuth2Authentication.setName(name);
    OAuth2AccessToken accessToken = tokenServices.createAccessToken(oAuth2Authentication);
    Optional<AccessToken> optionalAccessToken =
        accessTokenService.getAccessToken(accessToken.getTokenValue());
    if (optionalAccessToken.isEmpty()) {
      throw new ValidationException("创建 Token 失败");
    }
    return accessTokenConverter.toPersonalAccessToken(optionalAccessToken.get());
  }

  public boolean revokeToken(Long uid, String token) {
    JwtTokenPayload payload = JwtUtils.payload(token);
    OAuth2AccessToken accessToken = this.tokenServices.readAccessToken(token);
    if (!uid.equals(payload.getUserId())) {
      return false;
    }
    return this.tokenServices.revokeToken(accessToken.getTokenValue());
  }

  public boolean revokeSession(Long uid, Long id) {
    Optional<AccessToken> optionalAccessToken = this.accessTokenService.getAccessToken(id);
    AccessToken accessToken = optionalAccessToken.orElse(null);

    if (accessToken == null
        || !accessToken.getUser().getId().equals(uid)
        || TokenType.SESSION_ID != accessToken.getTokenType()) {
      return false;
    }

    return this.tokenServices.revokeToken(accessToken.getToken());
  }

  public boolean revokePersonalAccessToken(Long uid, Long id) {
    Optional<AccessToken> optionalAccessToken = this.accessTokenService.getAccessToken(id);
    AccessToken accessToken = optionalAccessToken.orElse(null);

    if (accessToken == null
        || !accessToken.getUser().getId().equals(uid)
        || TokenType.PERSONAL_ACCESS_TOKEN != accessToken.getTokenType()) {
      return false;
    }

    return this.tokenServices.revokeToken(accessToken.getToken());
  }
}
