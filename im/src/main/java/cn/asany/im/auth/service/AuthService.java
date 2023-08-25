package cn.asany.im.auth.service;

import cn.asany.autoconfigure.properties.OpenIMProperties.AdminUser;
import cn.asany.im.auth.graphql.type.Platform;
import cn.asany.im.auth.service.vo.*;
import cn.asany.im.error.OpenIMServerAPIException;
import cn.asany.im.error.ServerException;
import cn.asany.im.utils.OpenIMUtils;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * IM 授权服务
 *
 * @author limaofeng
 */
@Slf4j
@Getter
public class AuthService {

  public static final String CACHE_KEY = "OPEN_IM";

  private final StringRedisTemplate redisTemplate;
  private final ValueOperations<String, String> valueOperations;

  private final String url;
  private final String secret;
  private final AdminUser admin;

  public AuthService(
      StringRedisTemplate redisTemplate, String url, String secret, AdminUser admin) {
    this.redisTemplate = redisTemplate;
    this.valueOperations = redisTemplate.opsForValue();

    this.url = url;
    this.secret = secret;
    this.admin = admin;
  }

  public ParseTokenData parseToken(String token, ParseTokenRequestBody request)
      throws OpenIMServerAPIException {
    String url = this.url + "/auth/parse_token";
    try {
      HttpResponse<ParseTokenResponseBody> response =
          Unirest.post(url)
              .header("token", token)
              .body(JSON.serialize(request))
              .asObject(ParseTokenResponseBody.class);
      return OpenIMUtils.getData(response.getBody());
    } catch (UnirestException e) {
      throw new ServerException(e.getMessage());
    }
  }

  public void forceLogout(String token, ForceLogoutRequestBody request)
      throws OpenIMServerAPIException {
    String url = this.url + "/auth/force_logout";
    try {
      HttpResponse<ForceLogoutResponseBody> response =
          Unirest.post(url)
              .header("token", token)
              .body(JSON.serialize(request))
              .asObject(ForceLogoutResponseBody.class);
      OpenIMUtils.getData(response.getBody());
    } catch (UnirestException e) {
      throw new ServerException(e.getMessage());
    }
  }

  /**
   * 换取管理员Token
   *
   * @param body 请求内容
   * @return UserTokenResult
   */
  public UserTokenData userToken(UserTokenRequestBody body) throws OpenIMServerAPIException {
    String url = this.url + "/auth/user_token";
    try {
      HttpResponse<UserTokenResponseBody> response =
          Unirest.post(url)
              .header("operationID", createTraceId())
              .body(JSON.serialize(body))
              .asObject(UserTokenResponseBody.class);
      return OpenIMUtils.getData(response.getBody());
    } catch (UnirestException e) {
      throw new ServerException(e.getMessage());
    }
  }

  public String adminToken() throws OpenIMServerAPIException {
    return this.token(Platform.Admin, this.admin.getUser());
  }

  public String token(Platform platform, String user) throws OpenIMServerAPIException {

    String cacheKey = CACHE_KEY + ":user_token:" + platform.name() + ":" + user;

    if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
      return redisTemplate.boundValueOps(cacheKey).get();
    }

    UserTokenData data =
        this.userToken(
            UserTokenRequestBody.builder()
                .secret(secret)
                .user(user)
                .platform(platform.getValue())
                .build());

    log.debug(data.toString());
    String token = data.getToken();

    Date expireAt = Date.from(Instant.now().plus(data.getExpiredTime(), ChronoUnit.SECONDS));

    this.valueOperations.set(cacheKey, token);
    redisTemplate.expireAt(cacheKey, expireAt);

    return token;
  }

  public String createTraceId() {
    return StringUtil.uuid();
  }
}
