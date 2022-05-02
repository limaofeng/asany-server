package cn.asany.base.openapi.apis;

import cn.asany.base.openapi.configs.AmapApiConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import java.util.List;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;

@Slf4j
public class AmapOpenAPI {
  private static final String BASE_URL = "https://restapi.amap.com/v3";

  private final AmapApiConfig amap;

  public AmapOpenAPI(AmapApiConfig amap) {
    this.amap = amap;
  }

  @SneakyThrows
  public IpResult ip(String ip) {
    String url = BASE_URL + "/ip";

    HttpRequest request =
        Unirest.get(url).queryString("output", "JSON").queryString("key", amap.getKey());

    if (StringUtil.isNotBlank(ip)) {
      request.queryString("ip", ip);
    }

    HttpResponse<IpResult> response = request.asObject(IpResult.class);
    IpResult body = response.getBody();

    if ("0".equals(body.getStatus())) {
      log.error("调用高德 OpenAPI /geocode/geo 失败,  响应结果为:" + body);
      throw new ValidationException("调用高德 OpenAPI /geocode/geo 失败");
    }

    return body;
  }

  @SneakyThrows
  public List<Geocode> geocode_geo(String addr) {
    return geocode_geo(addr, null);
  }

  @SneakyThrows
  public List<Geocode> geocode_geo(String addr, String city) {
    String url = BASE_URL + "/geocode/geo";

    HttpRequest request =
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
      log.error("调用高德 OpenAPI /geocode/geo 失败,  响应结果为:" + body);
      throw new ValidationException("调用高德 OpenAPI /geocode/geo 失败");
    }

    return JSON.getObjectMapper()
        .convertValue(jsonNode.get("geocodes"), new TypeReference<List<Geocode>>() {});
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
