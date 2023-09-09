package cn.asany.security.oauth.service;

import cn.asany.openapi.apis.AmapOpenAPI;
import cn.asany.openapi.service.OpenAPIService;
import cn.asany.security.oauth.domain.AccessToken;
import cn.asany.security.oauth.domain.AccessTokenClientDetails;
import cn.asany.security.oauth.domain.ClientDevice;
import cn.asany.security.oauth.job.TokenCleanupJob;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import lombok.SneakyThrows;
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
import org.jfantasy.schedule.service.TaskScheduler;
import org.quartz.*;
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

  private final TaskScheduler taskScheduler;

  public DataBaseTokenStore(
      StringRedisTemplate redisTemplate,
      AccessTokenService accessTokenService,
      OpenAPIService openApiService,
      TaskScheduler taskScheduler) {
    super(redisTemplate);
    this.accessTokenService = accessTokenService;
    this.openAPIService = openApiService;
    this.taskScheduler = taskScheduler;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void storeAccessToken(OAuth2AccessToken token, Authentication authentication) {
    Optional<AccessToken> optionalAccessToken =
        this.accessTokenService.getAccessToken(token.getTokenValue());

    HttpServletRequest request = ObjectUtil.getValue("details.request", authentication);

    AccessTokenClientDetails clientDetails = new AccessTokenClientDetails();

    if (request != null) {
      String ip = WebUtil.getRealIpAddress(request);
      AmapOpenAPI.IpResult ipResult = null;

      if (ip != null) {
        try {
          ipResult = openAPIService.getDefaultAmap().ip(ip);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
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
      log.debug(String.format("accessToken(%d) 保存成功!", accessToken.getId()));
    } else {
      AccessToken accessToken = optionalAccessToken.get();
      accessToken = this.accessTokenService.updateAccessToken(accessToken, token, clientDetails);
      log.debug(String.format("accessToken(%d) 更新成功!", accessToken.getId()));
    }
    super.storeAccessToken(token, authentication);
    this.scheduleTokenExpirationHandling(token.getTokenValue(), Date.from(token.getExpiresAt()));
  }

  @SneakyThrows(SchedulerException.class)
  private void scheduleTokenExpirationHandling(String tokenValue, Date expiresAt) {
    TriggerKey triggerKey = TokenCleanupJob.triggerKey(StringUtil.md5(tokenValue));
    Trigger trigger =
        TriggerBuilder.newTrigger()
            .forJob(TokenCleanupJob.JOBKEY_TOKEN_CLEANUP)
            .withIdentity(triggerKey)
            .usingJobData(TokenCleanupJob.jobData(tokenValue))
            .withDescription(
                TokenCleanupJob.triggerDescription(StringUtil.md5(tokenValue), expiresAt))
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    .withRepeatCount(0)
                    .withMisfireHandlingInstructionFireNow())
            .startAt(expiresAt)
            .build();
    if (this.taskScheduler.checkExists(triggerKey)) {
      this.taskScheduler.rescheduleJob(triggerKey, trigger);
    } else {
      this.taskScheduler.scheduleJob(trigger);
    }
  }

  @Override
  public void removeAccessToken(OAuth2AccessToken token) {
    this.accessTokenService.delete(token.getTokenValue());
    super.removeAccessToken(token);
  }
}
