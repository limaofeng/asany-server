package cn.asany.security.oauth.service;

import cn.asany.security.oauth.convert.AccessTokenConverter;
import cn.asany.security.oauth.domain.AccessToken;
import cn.asany.security.oauth.vo.PersonalAccessToken;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.security.SecurityContextHolder;
import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.oauth2.DefaultTokenServices;
import org.jfantasy.framework.security.oauth2.JwtTokenPayload;
import org.jfantasy.framework.security.oauth2.core.OAuth2AccessToken;
import org.jfantasy.framework.security.oauth2.core.OAuth2Authentication;
import org.jfantasy.framework.security.oauth2.core.OAuth2AuthenticationDetails;
import org.jfantasy.framework.security.oauth2.core.TokenType;
import org.jfantasy.framework.security.oauth2.jwt.JwtUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    oAuth2AuthenticationDetails.setTokenType(TokenType.PERSONAL);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    OAuth2Authentication oAuth2Authentication =
        new OAuth2Authentication(authentication, oAuth2AuthenticationDetails);
    oAuth2Authentication.setName(name);
    OAuth2AccessToken accessToken = tokenServices.createAccessToken(oAuth2Authentication);
    Optional<AccessToken> optionalAccessToken =
        accessTokenService.getAccessToken(accessToken.getTokenValue());
    if (!optionalAccessToken.isPresent()) {
      throw new ValidationException("创建 Token 失败");
    }
    return accessTokenConverter.toPersonalAccessToken(optionalAccessToken.get());
  }

  public boolean revokeToken(Long uid, String token) {
    JwtTokenPayload payload = JwtUtils.payload(token);
    OAuth2AccessToken accessToken = this.tokenServices.readAccessToken(token);
    if (!uid.equals(payload.getUid())) {
      return false;
    }
    return this.tokenServices.revokeToken(accessToken.getTokenValue());
  }

  public boolean revokeSession(Long uid, Long id) {
    Optional<AccessToken> optionalAccessToken = this.accessTokenService.getAccessToken(id);
    AccessToken accessToken = optionalAccessToken.orElse(null);

    if (accessToken == null
        || !accessToken.getUser().getId().equals(uid)
        || TokenType.SESSION != accessToken.getTokenType()) {
      return false;
    }

    return this.tokenServices.revokeToken(accessToken.getToken());
  }

  public boolean revokePersonalAccessToken(Long uid, Long id) {
    Optional<AccessToken> optionalAccessToken = this.accessTokenService.getAccessToken(id);
    AccessToken accessToken = optionalAccessToken.orElse(null);

    if (accessToken == null
        || !accessToken.getUser().getId().equals(uid)
        || TokenType.PERSONAL != accessToken.getTokenType()) {
      return false;
    }

    return this.tokenServices.revokeToken(accessToken.getToken());
  }
}
