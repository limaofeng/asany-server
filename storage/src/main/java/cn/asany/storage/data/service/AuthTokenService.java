/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.storage.data.service;

import cn.asany.base.utils.UUID;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import net.asany.jfantasy.framework.jackson.JSON;
import net.asany.jfantasy.framework.util.common.StringUtil;
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
