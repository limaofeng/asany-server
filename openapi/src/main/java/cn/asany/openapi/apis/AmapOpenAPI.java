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
package cn.asany.openapi.apis;

import cn.asany.openapi.configs.AmapApiConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kong.unirest.*;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.error.ValidationException;
import net.asany.jfantasy.framework.jackson.JSON;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * amap OpenAPI
 *
 * @author limaofeng
 */
@Slf4j
public class AmapOpenAPI {
  private static final String BASE_URL = "https://restapi.amap.com/v3";
  public static final String CACHE_KEY = "OPENAPI_AMAP";

  private final StringRedisTemplate redisTemplate;
  private final ValueOperations<String, String> valueOperations;

  private final AmapApiConfig amap;

  public AmapOpenAPI(StringRedisTemplate redisTemplate, AmapApiConfig amap) {
    this.redisTemplate = redisTemplate;
    this.valueOperations = redisTemplate.opsForValue();
    this.amap = amap;
  }

  @SneakyThrows(UnirestException.class)
  public IpResult ip(String ip) {

    String cacheKey = CACHE_KEY + ":ip:" + ip;
    if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
      return JSON.deserialize(redisTemplate.boundValueOps(cacheKey).get(), IpResult.class);
    }

    String url = BASE_URL + "/ip";

    HttpRequest<GetRequest> request =
        Unirest.get(url).queryString("output", "JSON").queryString("key", amap.getKey());

    if (StringUtil.isNotBlank(ip)) {
      request.queryString("ip", ip);
    }

    HttpResponse<String> response = request.asString();
    String body = response.getBody().replace("[]", "null");
    IpResult result = JSON.deserialize(body, IpResult.class);

    if ("0".equals(result.getStatus())) {
      log.error("调用高德 OpenAPI /ip 失败,  响应结果为:{}", body);
      throw new ValidationException("调用高德 OpenAPI /geocode/geo 失败");
    }

    valueOperations.set(cacheKey, body);
    redisTemplate.expire(cacheKey, 7, TimeUnit.DAYS);
    return result;
  }

  public List<Geocode> geocode_geo(String addr) {
    return geocode_geo(addr, null);
  }

  @SneakyThrows(UnirestException.class)
  public List<Geocode> geocode_geo(String addr, String city) {
    String url = BASE_URL + "/geocode/geo";

    HttpRequest<GetRequest> request =
        Unirest.get(url)
            .queryString("address", addr)
            .queryString("output", "JSON")
            .queryString("key", amap.getKey());

    if (StringUtil.isNotBlank(city)) {
      request.queryString("city", city);
    }

    HttpResponse<String> response = request.asString();
    String body = response.getBody();

    JsonNode jsonNode = JSON.deserialize(body.replace("[]", "null"));

    if ("0".equals(jsonNode.get("status").asText())) {
      log.error("调用高德 OpenAPI /geocode/geo 失败,  响应结果为:{}", body);
      throw new ValidationException("调用高德 OpenAPI /geocode/geo 失败");
    }

    return JSON.getObjectMapper().convertValue(jsonNode.get("geocodes"), new TypeReference<>() {});
  }

  @Data
  @ToString
  public static class IpResult {
    private String status;
    private String info;
    private String infocode;
    private String province;
    private String city;
    private String adcode;
    private String rectangle;
  }

  @Data
  public static class Geocode {
    private String formatted_address;
    private String country;
    private String province;
    private String citycode;
    private String city;
    private String district;
    private String township;
    private Neighborhood neighborhood;
    private building building;
    private String adcode;
    private String street;
    private String number;
    private String location;
    private String level;
  }

  @Data
  public static class Neighborhood {
    private String name;
    private String type;
  }

  @Data
  public static class building {
    private String name;
    private String type;
  }
}
