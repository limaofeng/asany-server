package cn.asany.security.core.service;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.TestApplication;
import cn.asany.security.core.domain.AuthorizedService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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
class AuthorizedServiceServiceTest {

  @Autowired private AuthorizedServiceService authorizedServiceService;

  @BeforeEach
  void setUp() {}

  public void init() {
    AuthorizedService service = AuthorizedService.builder().name("内容管理").build();
    authorizedServiceService.save(service);
  }
}
