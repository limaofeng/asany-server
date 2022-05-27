package cn.asany.shanhai.gateway.service;

import cn.asany.shanhai.TestApplication;
import cn.asany.shanhai.gateway.domain.Service;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
class ServiceRegistryServiceTest {

  @Autowired private ServiceRegistryService serviceRegistryService;

  @Test
  void services() {
    this.serviceRegistryService.services();
  }

  @Test
  void addService() {
    Service service =
        Service.builder()
            .code("party-building")
            .name("党建")
            .url("http://dj.prod.thuni-h.com/graphql")
            .build();
    this.serviceRegistryService.addService(service);
  }
}
