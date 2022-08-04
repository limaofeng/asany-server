package cn.asany.im.auth.service;

import cn.asany.autoconfigure.properties.AdminUser;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

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

  /**
   * 用户注册
   *
   * @param request UserRegisterRequest
   * @return UserRegisterResponse
   */
  public UserRegisterData userRegister(UserRegisterRequestBody request)
      throws OpenIMServerAPIException {
    String url = this.url + "/auth/user_register";
    try {
      HttpResponse<UserRegisterResponseBody> response =
          Unirest.post(url).body(JSON.serialize(request)).asObject(UserRegisterResponseBody.class);
      return OpenIMUtils.getData(response.getBody());
    } catch (UnirestException e) {
      throw new ServerException(e.getMessage());
    }
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
          Unirest.post(url).body(JSON.serialize(body)).asObject(UserTokenResponseBody.class);
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
                .userID(user)
                .platform(platform.getValue())
                .build());
    log.debug(data.toString());
    String _token = data.getToken();

    Date expireAt = Date.from(Instant.now().plus(data.getExpiredTime(), ChronoUnit.SECONDS));

    this.valueOperations.set(cacheKey, _token);
    redisTemplate.expireAt(cacheKey, expireAt);

    return _token;
  }
}
