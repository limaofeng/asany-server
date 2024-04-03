package cn.asany.nuwa.template.service;

import cn.asany.nuwa.TestApplication;
import cn.asany.nuwa.template.domain.ApplicationTemplate;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.jackson.JSON;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StreamUtils;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApplicationTemplateServiceTest {

  @Autowired private ApplicationTemplateService applicationTemplateService;

  @BeforeEach
  void setUp() {}

  @Test
  void createApplicationTemplate() throws IOException {
    String text =
        StreamUtils.copyToString(
            ApplicationTemplate.class.getResourceAsStream("/app-web-template.json"),
            StandardCharsets.UTF_8);
    ApplicationTemplate applicationTemplate = JSON.deserialize(text, ApplicationTemplate.class);
    applicationTemplate =
        this.applicationTemplateService.createApplicationTemplate(applicationTemplate);
    log.debug(String.format("名称 %s ", applicationTemplate.getName()));
  }

  @Test
  void updateApplicationTemplate() throws IOException {
    String text =
        StreamUtils.copyToString(
            ApplicationTemplate.class.getResourceAsStream("/app-web-template.json"),
            StandardCharsets.UTF_8);
    ApplicationTemplate applicationTemplate = JSON.deserialize(text, ApplicationTemplate.class);
    applicationTemplate.setId(1L);
    applicationTemplate =
        this.applicationTemplateService.updateApplicationTemplate(applicationTemplate);
    log.debug(String.format("名称 %s ", applicationTemplate.getName()));
  }

  @Test
  void deleteApplicationTemplate() {
    List<ApplicationTemplate> applicationTemplates = this.applicationTemplateService.findAll();

    for (ApplicationTemplate application : applicationTemplates) {
      this.applicationTemplateService.deleteApplicationTemplate(application.getId());
    }
  }
}
