package cn.asany.security.oauth.service;

import cn.asany.security.oauth.bean.AccessToken;
import cn.asany.security.oauth.convert.AccessTokenConverter;
import cn.asany.security.oauth.dao.AccessTokenDao;
import cn.asany.security.oauth.vo.PersonalAccessToken;
import cn.asany.security.oauth.vo.SessionAccessToken;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * 访问令牌服务
 *
 * @author limaofeng
 */
@Service
public class AccessTokenService {

  private final AccessTokenDao accessTokenDao;
  private final DefaultTokenServices tokenServices;
  private final AccessTokenConverter accessTokenConverter;

  public AccessTokenService(
      AccessTokenDao accessTokenDao,
      @Autowired(required = false) DefaultTokenServices tokenServices,
      AccessTokenConverter accessTokenConverter) {
    this.accessTokenDao = accessTokenDao;
    this.tokenServices = tokenServices;
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
        this.accessTokenDao.findOne(
            PropertyFilter.builder().equal("token", accessToken.getTokenValue()).build());
    if (!optionalAccessToken.isPresent()) {
      throw new ValidationException("创建 Token 失败");
    }
    return accessTokenConverter.toPersonalAccessToken(optionalAccessToken.get());
  }

  public List<PersonalAccessToken> getPersonalAccessTokens(String clientId, Long uid) {
    PropertyFilterBuilder builder =
        PropertyFilter.builder()
            .equal("client.id", clientId)
            .equal("tokenType", TokenType.PERSONAL)
            .equal("user.id", uid);
    List<AccessToken> accessTokens = this.accessTokenDao.findAll(builder.build());
    return accessTokenConverter.toPersonalAccessTokens(accessTokens);
  }

  public SessionAccessToken getSessionById(Long id) {
    Optional<AccessToken> optional = this.accessTokenDao.findById(id);
    return optional.map(accessTokenConverter::toSession).orElse(null);
  }

  public List<SessionAccessToken> getSessions(String clientId, Long uid) {
    PropertyFilterBuilder builder =
        PropertyFilter.builder()
            .equal("client", clientId)
            .equal("tokenType", TokenType.SESSION)
            .equal("user.id", uid);
    List<AccessToken> accessTokens =
        this.accessTokenDao.findAll(builder.build(), Sort.by("issuedAt").descending());
    return accessTokenConverter.toSessions(accessTokens);
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
    PropertyFilterBuilder builder =
        PropertyFilter.builder()
            .equal("id", id)
            .equal("user.id", uid)
            .equal("tokenType", TokenType.SESSION);
    Optional<AccessToken> optionalAccessToken = this.accessTokenDao.findOne(builder.build());
    return optionalAccessToken
        .filter(accessToken -> this.tokenServices.revokeToken(accessToken.getToken()))
        .isPresent();
  }

  public boolean revokePersonalAccessToken(Long uid, Long id) {
    PropertyFilterBuilder builder =
        PropertyFilter.builder()
            .equal("id", id)
            .equal("user.id", uid)
            .equal("tokenType", TokenType.PERSONAL);
    Optional<AccessToken> optionalAccessToken = this.accessTokenDao.findOne(builder.build());
    return optionalAccessToken
        .filter(accessToken -> this.tokenServices.revokeToken(accessToken.getToken()))
        .isPresent();
  }
}
