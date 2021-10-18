package cn.asany.organization.core.service;

import cn.asany.TestApplication;
import cn.asany.organization.core.bean.Organization;
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
class OrganizationServiceTest {

  @Autowired private OrganizationService organizationService;

  @Test
  void save() {
    Organization organization = Organization.builder().code("asany").name("管理端").build();
    organizationService.save(organization);
    log.debug("新增成功:" + organization.getId());
  }

  @Test
  void get() {}

  @Test
  void update() {}

  @Test
  void delete() {}

  @Test
  void findAll() {}
}
