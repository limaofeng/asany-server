package cn.asany.nuwa.app.service;

import cn.asany.nuwa.TestApplication;
import cn.asany.nuwa.app.bean.Routespace;
import cn.asany.nuwa.template.bean.ApplicationTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RoutespaceServiceTest {

    @Autowired
    private RoutespaceService routespaceService;

    @Test
    void createRoutespace() {
        this.routespaceService.createRoutespace(Routespace.builder()
            .id("web")
            .name("PC Web")
            .applicationTemplate(ApplicationTemplate.builder().id(4L).build()).build());
    }
}