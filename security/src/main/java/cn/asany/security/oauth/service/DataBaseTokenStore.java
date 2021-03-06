package cn.asany.security.oauth.service;

import cn.asany.openapi.apis.AmapOpenAPI;
import cn.asany.openapi.service.OpenAPIService;
import cn.asany.security.oauth.domain.AccessToken;
import cn.asany.security.oauth.domain.AccessTokenClientDetails;
import cn.asany.security.oauth.domain.ClientDevice;
import eu.bitwalker.useragentutils.UserAgent;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.oauth2.JwtTokenPayload;
import org.jfantasy.framework.security.oauth2.core.AbstractTokenStore;
import org.jfantasy.framework.security.oauth2.core.OAuth2AccessToken;
import org.jfantasy.framework.security.oauth2.core.OAuth2AuthenticationDetails;
import org.jfantasy.framework.security.oauth2.jwt.JwtUtils;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TokenStore
 *
 * @author limaofeng
 */
@Slf4j
@Service
public class DataBaseTokenStore extends AbstractTokenStore {

  private final AccessTokenService accessTokenService;
  private final OpenAPIService openAPIService;

  public DataBaseTokenStore(
      StringRedisTemplate redisTemplate,
      AccessTokenService accessTokenService,
      OpenAPIService openAPIService) {
    super(redisTemplate);
    this.accessTokenService = accessTokenService;
    this.openAPIService = openAPIService;
  }

  @Override
  @Transactional
  public void storeAccessToken(OAuth2AccessToken token, Authentication authentication) {
    Optional<AccessToken> optionalAccessToken =
        this.accessTokenService.getAccessToken(token.getTokenValue());

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

    // ????????????????????????????????????????????????????????????
    if (!optionalAccessToken.isPresent()) {
      JwtTokenPayload payload = JwtUtils.payload(token.getTokenValue());

      clientDetails.setLastIp(clientDetails.getIp());
      clientDetails.setLastLocation(clientDetails.getLocation());

      OAuth2AuthenticationDetails details =
          (OAuth2AuthenticationDetails) authentication.getDetails();

      AccessToken accessToken =
          this.accessTokenService.createAccessToken(
              ObjectUtil.defaultValue(authentication.getName(), payload::getName),
              payload.getUid(),
              payload.getClientId(),
              details.getClientSecret(),
              token,
              clientDetails);
      log.debug(String.format("accessToken(%d) ????????????!", accessToken.getId()));
    } else {
      AccessToken accessToken = optionalAccessToken.get();
      accessToken = this.accessTokenService.updateAccessToken(accessToken, token, clientDetails);
      log.debug(String.format("accessToken(%d) ????????????!", accessToken.getId()));
    }

    super.storeAccessToken(token, authentication);
  }

  @Override
  public void removeAccessToken(OAuth2AccessToken token) {
    this.accessTokenService.delete(token.getTokenValue());
    super.removeAccessToken(token);
  }
}
