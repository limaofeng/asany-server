package cn.asany.security.core.service;

import cn.asany.TestApplication;
import cn.asany.security.core.domain.Role;
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
class RoleServiceTest {

  @Autowired private RoleService roleService;

  @Test
  void defaultRoles() {
    if (!this.roleService.findById(Role.USER.getId()).isPresent()) {
      this.roleService.save(Role.USER);
    }
    if (!this.roleService.findById(Role.ADMIN.getId()).isPresent()) {
      this.roleService.save(Role.ADMIN);
    }
  }
}
