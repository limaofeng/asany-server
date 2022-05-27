package cn.asany.nuwa.app.service;

import cn.asany.nuwa.TestApplication;
import cn.asany.nuwa.app.domain.Routespace;
import java.util.List;
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
  void clearRoutespace() {
    List<Routespace> routespaces = routespaceService.findAll();
    for (Routespace routespace : routespaces) {
      routespaceService.deleteRoutespace(routespace.getId());
    }
  }

  @Test
  void createRoutespace() {
    this.routespaceService.createRoutespace(Routespace.DEFAULT_ROUTESPACE_WEB);
    this.routespaceService.createRoutespace(Routespace.DEFAULT_ROUTESPACE_WAP);
  }
}
