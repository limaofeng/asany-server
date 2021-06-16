package cn.asany.nuwa.template.service;

import cn.asany.nuwa.TestApplication;
import cn.asany.nuwa.template.bean.ApplicationTemplate;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApplicationTemplateServiceTest {

    @Autowired
    private ApplicationTemplateService applicationTemplateService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createApplicationTemplate() throws IOException {
        String text = FileUtil.readFile(ApplicationTemplate.class.getResourceAsStream("/app-web-template.json"));
        ApplicationTemplate applicationTemplate = JSON.deserialize(text, ApplicationTemplate.class);
        this.applicationTemplateService.createApplicationTemplate(applicationTemplate);
        log.debug(String.format("名称 %s ", applicationTemplate.getName()));
    }

    @Test
    void deleteApplicationTemplate() throws IOException {
        List<ApplicationTemplate> applicationTemplates = this.applicationTemplateService.findAll();
        for (ApplicationTemplate application : applicationTemplates) {
            this.applicationTemplateService.deleteApplicationTemplate(application.getId());
        }
    }

}