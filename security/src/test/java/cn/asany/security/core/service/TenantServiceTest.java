package cn.asany.security.core.service;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.TestApplication;
import cn.asany.security.core.domain.Tenant;
import cn.asany.security.core.domain.User;
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
class TenantServiceTest {

  @Autowired private TenantService tenantService;

  @Test
  void save() {
    tenantService.save(
        Tenant.builder()
            .domain("xxx.asany.net")
            .mainAccount(User.builder().id(1L).build())
            .build());
  }
}
