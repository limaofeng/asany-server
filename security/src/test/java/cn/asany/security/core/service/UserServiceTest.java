package cn.asany.security.core.service;

import cn.asany.TestApplication;
import cn.asany.security.core.domain.User;
import cn.asany.security.core.domain.enums.UserType;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
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
class UserServiceTest {

  @Autowired private UserService userService;

  private final Set<Long> ids = new HashSet<>();

  @BeforeEach
  void init() {}

  @AfterEach
  void clear() {
    this.userService.delete(this.ids);
  }

  @Test
  void save() {
    User user =
        User.builder().username("asany_admin").password("123456").userType(UserType.ADMIN).build();
    this.userService.save(user);
    assert user.getId() != null;
    this.ids.add(user.getId());
  }
}
