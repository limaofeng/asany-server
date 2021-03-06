package cn.asany.nuwa.template.service;

import cn.asany.nuwa.TestApplication;
import cn.asany.nuwa.app.domain.Routespace;
import cn.asany.nuwa.app.service.RoutespaceService;
import cn.asany.nuwa.template.domain.ApplicationTemplate;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.junit.jupiter.api.BeforeEach;
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
class ApplicationTemplateServiceTest {

  @Autowired private RoutespaceService routespaceService;
  @Autowired private ApplicationTemplateService applicationTemplateService;

  @BeforeEach
  void setUp() {}

  @Test
  void createApplicationTemplate() throws IOException {
    String text =
        FileUtil.readFile(ApplicationTemplate.class.getResourceAsStream("/app-web-template.json"));
    ApplicationTemplate applicationTemplate = JSON.deserialize(text, ApplicationTemplate.class);
    applicationTemplate =
        this.applicationTemplateService.createApplicationTemplate(applicationTemplate);
    log.debug(String.format("名称 %s ", applicationTemplate.getName()));
  }

  @Test
  void updateApplicationTemplate() throws IOException {
    String text =
        FileUtil.readFile(ApplicationTemplate.class.getResourceAsStream("/app-web-template.json"));
    ApplicationTemplate applicationTemplate = JSON.deserialize(text, ApplicationTemplate.class);
    applicationTemplate.setId(1L);
    applicationTemplate =
        this.applicationTemplateService.updateApplicationTemplate(applicationTemplate);
    log.debug(String.format("名称 %s ", applicationTemplate.getName()));
  }

  @Test
  void deleteApplicationTemplate() {
    List<Routespace> routespaces = routespaceService.findAll();
    List<ApplicationTemplate> applicationTemplates = this.applicationTemplateService.findAll();

    for (ApplicationTemplate application : applicationTemplates) {
      if (ObjectUtil.exists(routespaces, "applicationTemplate.id", application.getId())) {
        continue;
      }
      this.applicationTemplateService.deleteApplicationTemplate(application.getId());
    }
  }
}
