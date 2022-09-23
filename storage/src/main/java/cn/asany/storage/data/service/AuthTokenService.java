package cn.asany.storage.data.service;

import cn.asany.base.utils.UUID;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service("storage.AuthTokenService")
public class AuthTokenService {

  private final String STORAGE_AUTH_TOKEN_PREFIX = "storage_auth_token:";

  private final StringRedisTemplate redisTemplate;
  private final ValueOperations<String, String> valueOperations;

  public AuthTokenService(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.valueOperations = redisTemplate.opsForValue();
  }

  public String storeToken(AuthToken authToken) {
    String token = UUID.getShortId();

    while (Boolean.TRUE.equals(this.redisTemplate.hasKey(token))) {
      token = UUID.getShortId();
    }

    String key = STORAGE_AUTH_TOKEN_PREFIX + token;

    String tokenValue = buildStoreData(authToken);

    StringUtil.shortUrl(tokenValue);

    this.valueOperations.set(key, tokenValue);
    redisTemplate.expire(key, 5, TimeUnit.MINUTES);
    return token;
  }

  public AuthToken readToken(String token) {
    String date = this.valueOperations.get(STORAGE_AUTH_TOKEN_PREFIX + token);
    if (StringUtil.isBlank(date)) {
      return null;
    }
    return buildAuthToken(date);
  }

  private String buildStoreData(AuthToken token) {
    Map<String, Object> data = new HashMap<>();
    data.put("personal_token", token.getPersonalToken());
    data.put("user", token.getUser());
    data.put("path", token.getPath());
    return JSON.serialize(data);
  }

  private AuthToken buildAuthToken(String data) {
    HashMap object = JSON.deserialize(data, HashMap.class);
    return AuthToken.builder()
        .user(Long.valueOf(object.get("user").toString()))
        .path(object.get("object").toString())
        .personalToken(object.get("personal_token").toString())
        .build();
  }
}
