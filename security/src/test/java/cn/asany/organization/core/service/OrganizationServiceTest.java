package cn.asany.organization.core.service;

import cn.asany.TestApplication;
import cn.asany.organization.core.domain.Organization;
import cn.asany.organization.core.domain.enums.MemberRole;
import cn.asany.organization.core.service.dto.NuwaOrganization;
import cn.asany.organization.employee.domain.Employee;
import cn.asany.organization.employee.service.EmployeeService;
import cn.asany.security.core.domain.User;
import cn.asany.security.core.service.UserService;
import java.io.InputStream;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.yaml.snakeyaml.Yaml;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class OrganizationServiceTest {

  @Autowired private OrganizationService organizationService;
  @Autowired private EmployeeService employeeService;
  @Autowired private UserService userService;

  @Test
  void createOrganization() {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("org.yml");
    // 调基础工具类的方法
    Yaml yaml = new Yaml();
    NuwaOrganization org = yaml.loadAs(inputStream, NuwaOrganization.class);
    assert org.getCode().equals("hotsoon");
    this.organizationService.deleteByCode(org.getCode());
    Organization organization = this.organizationService.createOrganization(org);
    Assertions.assertNotNull(organization.getId());
  }

  @Test
  void save() {
    Organization organization = Organization.builder().code("hotsoon").name("管理端").build();

    Optional<User> optionalUser = this.userService.findOneByUsername("limaofeng");

    if (!optionalUser.isPresent()) {
      return;
    }

    Optional<Organization> optional = organizationService.findOneByCode(organization.getCode());
    if (!optional.isPresent()) {
      organizationService.save(organization);
      optional = Optional.of(organization);
    }

    Optional<Employee> optionalEmployee = employeeService.findOneByUser(optionalUser.get().getId());

    if (!optionalEmployee.isPresent()) {
      Employee employee =
          Employee.builder(optionalUser.get())
              .addEmail("默认", "limaofeng@msn.com")
              .addPhone("默认", "15921884771")
              .build();

      employeeService.save(optional.get().getId(), MemberRole.ADMIN, employee);
    }

    log.debug("新增成功:" + organization.getCode());
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
