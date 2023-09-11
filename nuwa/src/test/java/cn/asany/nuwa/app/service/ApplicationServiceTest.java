package cn.asany.nuwa.app.service;

import cn.asany.nuwa.TestApplication;
import cn.asany.nuwa.app.domain.Application;
import cn.asany.nuwa.app.service.dto.NativeApplication;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.security.oauth2.core.ClientSecretType;
import org.jfantasy.framework.util.common.StringUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApplicationServiceTest {

  @Autowired private ApplicationService applicationService;

  @Test
  void createApplication() {
    NativeApplication app = NativeApplication.builder().name("管理台").routespace("web").build();
    Application application = applicationService.createApplication(app, 1L);

    log.debug(
        String.format(
            "应用 %s 已经创建成功，ClientId = %s ClientSecret = %s",
            application.getName(),
            application.getClientId(),
            application.getClientSecret(ClientSecretType.SESSION)));
  }

  @Test
  void deleteApplication() {
    List<Application> applications = applicationService.findAll(PropertyFilter.newFilter());
    applications.forEach(item -> applicationService.deleteApplication(item.getId()));
  }

  @Test
  void generateClientSecret() {
    String clientId = StringUtil.generateNonceString(ApplicationService.NONCE_CHARS, 20);
    String clientSecretStr = StringUtil.generateNonceString(ApplicationService.NONCE_CHARS, 40);
    log.debug("clientId:" + clientId);
    log.debug("clientId:" + clientSecretStr);
  }
}
