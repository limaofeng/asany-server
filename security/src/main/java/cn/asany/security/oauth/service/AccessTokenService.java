package cn.asany.security.oauth.service;

import cn.asany.security.core.domain.User;
import cn.asany.security.oauth.convert.AccessTokenConverter;
import cn.asany.security.oauth.dao.AccessTokenDao;
import cn.asany.security.oauth.domain.AccessToken;
import cn.asany.security.oauth.domain.AccessTokenClientDetails;
import cn.asany.security.oauth.vo.PersonalAccessToken;
import cn.asany.security.oauth.vo.SessionAccessToken;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.security.oauth2.core.OAuth2AccessToken;
import org.jfantasy.framework.security.oauth2.core.TokenType;
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
  private final AccessTokenConverter accessTokenConverter;

  public AccessTokenService(
      AccessTokenDao accessTokenDao, AccessTokenConverter accessTokenConverter) {
    this.accessTokenDao = accessTokenDao;
    this.accessTokenConverter = accessTokenConverter;
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

  /**
   * 创建 AccessToken
   *
   * @param name 名称
   * @param uid 用户ID
   * @param clientId 客户端
   * @param clientSecret 客户端密钥
   * @param token 令牌
   * @param clientDetails 请求详情
   * @return AccessToken
   */
  public AccessToken createAccessToken(
      String name,
      Long uid,
      String clientId,
      String clientSecret,
      OAuth2AccessToken token,
      AccessTokenClientDetails clientDetails) {
    return this.accessTokenDao.save(
        AccessToken.builder()
            .name(name)
            .token(token.getTokenValue())
            .tokenType(token.getTokenType())
            .issuedAt(Date.from(token.getIssuedAt()))
            .expiresAt(token.getExpiresAt() != null ? Date.from(token.getExpiresAt()) : null)
            .scopes(token.getScopes())
            .refreshToken(token.getRefreshTokenValue())
            .client(clientId)
            .clientSecret(clientSecret)
            .lastUsedTime(Date.from(Instant.now()))
            .user(User.builder().id(uid).build())
            .clientDetails(clientDetails)
            .build());
  }

  /**
   * 更新 AccessToken
   *
   * @param accessToken 原 AccessToken
   * @param token 令牌
   * @param clientDetails 请求详情
   * @return AccessToken
   */
  public AccessToken updateAccessToken(
      AccessToken accessToken, OAuth2AccessToken token, AccessTokenClientDetails clientDetails) {
    accessToken.setExpiresAt(token.getExpiresAt() != null ? Date.from(token.getExpiresAt()) : null);

    AccessTokenClientDetails _clientDetails = accessToken.getClientDetails();
    if (_clientDetails == null) {
      _clientDetails = new AccessTokenClientDetails();
      _clientDetails.setDevice(clientDetails.getDevice());
    }
    _clientDetails.setLastIp(clientDetails.getIp());
    _clientDetails.setLastLocation(clientDetails.getLocation());
    accessToken.setClientDetails(_clientDetails);
    accessToken.setLastUsedTime(Date.from(Instant.now()));
    this.accessTokenDao.update(accessToken);
    return accessToken;
  }

  public Optional<AccessToken> getAccessToken(Long id) {
    return this.accessTokenDao.findById(id);
  }

  public Optional<AccessToken> getAccessToken(String token) {
    return this.accessTokenDao.findOne(PropertyFilter.builder().equal("token", token).build());
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

  /**
   * 查询密钥最后使用时间
   *
   * @param client 客户端
   * @param clientSecret 客户端密钥
   * @return Date
   */
  public Date getLastUseTime(String client, String clientSecret) {
    List<AccessToken> accessTokens =
        this.accessTokenDao.findAll(
            PropertyFilter.builder()
                .equal("client", client)
                .equal("clientSecret", clientSecret)
                .build(),
            1,
            Sort.by("lastUsedTime").descending());
    if (accessTokens.isEmpty()) {
      return null;
    }
    return accessTokens.get(0).getLastUsedTime();
  }

  public void delete(String token) {
    Optional<AccessToken> accessToken = getAccessToken(token);
    accessToken.ifPresent(this.accessTokenDao::delete);
  }
}
