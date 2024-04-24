package cn.asany.openapi.configs;

import static org.junit.jupiter.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.jackson.JSON;
import org.junit.jupiter.api.Test;

@Slf4j
class WeixinConfigTest {

  @Test
  void print() {
    WeixinConfig config =
        WeixinConfig.builder()
            .appId("wx59114ee8f0ca3806")
            .appSecret("c5f6bd30f31c051155ac3bba094b423f")
            .build();
    log.debug(JSON.serialize(config));
  }
}
