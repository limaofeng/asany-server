package cn.asany.nuwa.app.service;

import cn.asany.nuwa.TestApplication;
import cn.asany.nuwa.app.bean.Routespace;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RoutespaceServiceTest {

  @Autowired private RoutespaceService routespaceService;

  @Test
  void createRoutespace() {
    this.routespaceService.createRoutespace(
        Routespace.builder().id("web").name("PC Web").applicationTemplate(1L).build());

    this.routespaceService.createRoutespace(
        Routespace.builder().id("wap").name("Wap 网站").applicationTemplate(1L).build());
  }
}
