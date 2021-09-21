package cn.asany.nuwa.app.service;

import cn.asany.nuwa.TestApplication;
import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.service.dto.NativeApplication;
import java.io.InputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.StringUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.yaml.snakeyaml.Yaml;

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
            application.getName(), application.getClientId(), application.getClientSecret()));
  }

  @Test
  void createApplicationFromYaml() {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("app.yml");
    // 调基础工具类的方法
    Yaml yaml = new Yaml();
    NativeApplication app = yaml.loadAs(inputStream, NativeApplication.class);
    assert app.getName().equals("website");
    applicationService.deleteApplication(app.getName());
    Application application = applicationService.createApplication(app);
    log.debug(
        String.format(
            "应用 %s 已经创建成功，ClientId = %s ClientSecret = %s",
            application.getName(), application.getClientId(), application.getClientSecret()));
  }

  @Test
  void createAdminApplicationFromYaml() {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("app_admin.yml");
    // 调基础工具类的方法
    Yaml yaml = new Yaml();
    NativeApplication app = yaml.loadAs(inputStream, NativeApplication.class);
    assert app.getName().equals("admin");
    applicationService.deleteApplication(app.getName());
    Application application = applicationService.createApplication(app);
    log.debug(
        String.format(
            "应用 %s 已经创建成功，ClientId = %s ClientSecret = %s",
            application.getName(), application.getClientId(), application.getClientSecret()));
  }

  @Test
  void deleteApplication() {
    List<Application> applications = applicationService.findAll(PropertyFilter.builder().build());
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
