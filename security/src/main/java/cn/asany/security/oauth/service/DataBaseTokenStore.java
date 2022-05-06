package cn.asany.security.oauth.service;

import cn.asany.openapi.apis.AmapOpenAPI;
import cn.asany.openapi.service.OpenAPIService;
import cn.asany.security.core.bean.User;
import cn.asany.security.oauth.bean.AccessToken;
import cn.asany.security.oauth.bean.AccessTokenClientDetails;
import cn.asany.security.oauth.bean.ClientDevice;
import cn.asany.security.oauth.dao.AccessTokenDao;
import eu.bitwalker.useragentutils.UserAgent;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.oauth2.JwtTokenPayload;
import org.jfantasy.framework.security.oauth2.core.AbstractTokenStore;
import org.jfantasy.framework.security.oauth2.core.OAuth2AccessToken;
import org.jfantasy.framework.security.oauth2.jwt.JwtUtils;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * TokenStore
 *
 * @author limaofeng
 */
@Slf4j
@Service
public class DataBaseTokenStore extends AbstractTokenStore {

  private final AccessTokenDao accessTokenDao;
  private final OpenAPIService openAPIService;

  public DataBaseTokenStore(
      StringRedisTemplate redisTemplate,
      AccessTokenDao accessTokenDao,
      OpenAPIService openAPIService) {
    super(redisTemplate);
    this.accessTokenDao = accessTokenDao;
    this.openAPIService = openAPIService;
  }

  protected Optional<AccessToken> getAccessToken(String token) {
    return this.accessTokenDao.findOne(PropertyFilter.builder().equal("token", token).build());
  }

  @Override
  public void storeAccessToken(OAuth2AccessToken token, Authentication authentication) {
    Optional<AccessToken> optionalAccessToken = getAccessToken(token.getTokenValue());

    HttpServletRequest request = ObjectUtil.getValue("details.request", authentication);

    AccessTokenClientDetails clientDetails = new AccessTokenClientDetails();

    if (request != null) {
      String ip = WebUtil.getRealIpAddress(request);
      AmapOpenAPI.IpResult ipResult = null;

      try {
        ipResult = openAPIService.getDefaultAmap().ip(ip);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }

      UserAgent userAgent = WebUtil.parseUserAgent(request);
      clientDetails.setIp(ip);
      clientDetails.setDevice(
          ClientDevice.builder()
              .userAgent(request.getHeader("User-Agent"))
              .browser(userAgent.getBrowser().getName())
              .type(userAgent.getOperatingSystem().getDeviceType())
              .operatingSystem(userAgent.getOperatingSystem().getName())
              .build());
      if (ipResult != null && StringUtil.isNotBlank(ipResult.getCity())) {
        String location =
            ipResult.getCity().equals(ipResult.getProvince())
                ? ipResult.getCity()
                : StringUtil.defaultValue(ipResult.getProvince(), "") + " " + ipResult.getCity();
        clientDetails.setLocation(location.trim());
      }
    }

    // 如果已经存在，更新最后使用时间及位置信息
    if (!optionalAccessToken.isPresent()) {
      JwtTokenPayload payload = JwtUtils.payload(token.getTokenValue());

      clientDetails.setLastIp(clientDetails.getIp());
      clientDetails.setLastLocation(clientDetails.getLocation());

      this.accessTokenDao.save(
          AccessToken.builder()
              .name(ObjectUtil.defaultValue(authentication.getName(), payload::getName))
              .token(token.getTokenValue())
              .tokenType(token.getTokenType())
              .issuedAt(Date.from(token.getIssuedAt()))
              .expiresAt(token.getExpiresAt() != null ? Date.from(token.getExpiresAt()) : null)
              .scopes(token.getScopes())
              .refreshToken(token.getRefreshTokenValue())
              .client(payload.getClientId())
              .lastUsedTime(Date.from(Instant.now()))
              .user(User.builder().id(payload.getUid()).build())
              .clientDetails(clientDetails)
              .build());
    } else {
      AccessToken accessToken = optionalAccessToken.get();
      accessToken.setExpiresAt(
          token.getExpiresAt() != null ? Date.from(token.getExpiresAt()) : null);

      AccessTokenClientDetails _clientDetails = accessToken.getClientDetails();
      if (_clientDetails == null) {
        _clientDetails = new AccessTokenClientDetails();
        _clientDetails.setDevice(clientDetails.getDevice());
      }
      _clientDetails.setLastIp(clientDetails.getIp());
      _clientDetails.setLastLocation(clientDetails.getLocation());
      accessToken.setClientDetails(_clientDetails);
      accessToken.setLastUsedTime(Date.from(Instant.now()));
    }

    super.storeAccessToken(token, authentication);
  }

  @Override
  public void removeAccessToken(OAuth2AccessToken token) {
    Optional<AccessToken> optionalAccessToken = getAccessToken(token.getTokenValue());
    optionalAccessToken.ifPresent(this.accessTokenDao::delete);
    super.removeAccessToken(token);
  }
}
