package cn.asany.base.openapi.service;

import cn.asany.base.openapi.apis.AmapOpenAPI;
import cn.asany.base.openapi.configs.AmapApiConfig;
import com.mashape.unirest.http.Unirest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.jackson.UnirestObjectMapper;
import org.junit.jupiter.api.Test;

@Slf4j
class AmapOpenAPITest {

  @Test
  void geocode_geo() {
    Unirest.setObjectMapper(new UnirestObjectMapper(JSON.getObjectMapper()));
    AmapOpenAPI api =
        new AmapOpenAPI(AmapApiConfig.builder().key("724d735795516088493886bf9ce44395").build());
    List<AmapOpenAPI.Geocode> geocodes = api.geocode_geo("北京市复兴门内大街101号百盛购物中心南楼4层WMF专柜");
    //    List<AmapOpenAPI.Geocode> geocodes = api.geocode_geo("复兴门内大街101号百盛购物中心南楼4层WMF专柜",
    // "110102");
    for (AmapOpenAPI.Geocode geocode : geocodes) {
      String location = geocode.getLocation();
      String lng = StringUtils.substringBefore(location, ",");
      String lat = StringUtils.substringAfter(location, ",");
      log.debug(String.format("lng = %s , lat = %s", lng, lat));
    }
  }
}
