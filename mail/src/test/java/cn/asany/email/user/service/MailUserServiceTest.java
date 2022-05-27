package cn.asany.email.user.service;

import cn.asany.email.TestApplication;
import cn.asany.email.user.domain.MailUser;
import lombok.extern.slf4j.Slf4j;
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
class MailUserServiceTest {

  @Autowired private MailUserService userService;

  @Test
  void createUser() {}

  @Test
  void repairUser() {
    MailUser user = userService.repairUser(1L);
    log.debug("mail user: " + user.toString());
    user = userService.repairUser(2L);
    log.debug("mail user: " + user.toString());
  }
}
