package cn.asany.message.api;

import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.jackson.JSON;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class SMSSenderConfigTest {

  @Test
  void testToString() {
    SMSSenderConfig config = new SMSSenderConfig();
    config.setEndpoint("1");
    config.setAccessKeyId("b");
    config.setAccessKeySecret("c");

    log.debug(JSON.serialize(config));
  }
}
