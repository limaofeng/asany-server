package cn.asany.nuwa.app;

import cn.asany.cms.article.service.ArticleChannelService;
import cn.asany.cms.module.CmsModuleProperties;
import cn.asany.nuwa.YamlUtils;
import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.service.ApplicationService;
import cn.asany.nuwa.app.service.dto.NativeApplication;
import cn.asany.shanhai.TestApplication;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
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
  @Autowired private ArticleChannelService channelService;

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
    YamlUtils.addModuleClass("cms", CmsModuleProperties.class);
    NativeApplication app = YamlUtils.load(inputStream);
    assert app.getName().equals("admin");
    channelService.deleteAll();
    applicationService.deleteApplication(app.getName());
    Application application = applicationService.createApplication(app);
    log.debug(
        String.format(
            "应用 %s 已经创建成功，ClientId = %s ClientSecret = %s",
            application.getName(), application.getClientId(), application.getClientSecret()));
  }
}
