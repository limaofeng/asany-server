package cn.asany.message.api;

import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.jackson.JSON;
import org.junit.jupiter.api.Test;

@Slf4j
class SMSSenderConfigTest {

  @Test
  void testToString() {
    SMSChannelConfig config = new SMSChannelConfig();
    config.setEndpoint("1");
    config.setAccessKeyId("b");
    config.setAccessKeySecret("c");

    log.debug(JSON.serialize(config));
  }
}
