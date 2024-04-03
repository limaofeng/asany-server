package cn.asany.nuwa.app.service;

import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.common.StringUtil;
import org.junit.jupiter.api.Test;

@Slf4j
public class SerialNumber {
  @Test
  void generateClientSecret() {
    String clientId = StringUtil.generateNonceString(ApplicationService.NONCE_CHARS, 20);
    String clientSecretStr = StringUtil.generateNonceString(ApplicationService.NONCE_CHARS, 40);
    log.debug("clientId: " + clientId);
    log.debug("clientSecret: " + clientSecretStr);
  }
}
