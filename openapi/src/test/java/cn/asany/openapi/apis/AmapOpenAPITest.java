package cn.asany.openapi.apis;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.openapi.configs.AmapApiConfig;
import java.util.List;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.jackson.JSON;
import net.asany.jfantasy.framework.jackson.UnirestObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class AmapOpenAPITest {

  AmapOpenAPI api;

  @BeforeEach
  void setUp() {
    Unirest.config().setObjectMapper(new UnirestObjectMapper(JSON.getObjectMapper()));
    api = new AmapOpenAPI(AmapApiConfig.builder().key("724d735795516088493886bf9ce44395").build());
  }

  @Test
  void testGeocode_geo() {
    List<AmapOpenAPI.Geocode> geocodes = api.geocode_geo("北京市复兴门内大街101号百盛购物中心南楼4层WMF专柜");
    for (AmapOpenAPI.Geocode geocode : geocodes) {
      String location = geocode.getLocation();
      String lng = StringUtils.substringBefore(location, ",");
      String lat = StringUtils.substringAfter(location, ",");
      log.debug(String.format("lng = %s , lat = %s", lng, lat));
    }
  }

  @Test
  void testGeocode_geo1() {
    List<AmapOpenAPI.Geocode> geocodes = api.geocode_geo("复兴门内大街101号百盛购物中心南楼4层WMF专柜", "110102");
    for (AmapOpenAPI.Geocode geocode : geocodes) {
      String location = geocode.getLocation();
      String lng = StringUtils.substringBefore(location, ",");
      String lat = StringUtils.substringAfter(location, ",");
      log.debug(String.format("lng = %s , lat = %s", lng, lat));
    }
  }

  @Test
  void ip() {
    AmapOpenAPI.IpResult result = api.ip("183.195.20.59");
    log.debug("result:" + result.toString());
    assertTrue(result.getCity().endsWith("上海市"));
  }
}
