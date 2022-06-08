package cn.asany.website.data.service;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.organization.core.domain.Organization;
import cn.asany.website.TestApplication;
import cn.asany.website.data.domain.Website;
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
class WebsiteServiceTest {

  @Autowired private WebsiteService websiteService;

  @Test
  void create() {
    Website website = Website.builder().build();
    website.setLogo(null);
    website.setName("测试网站");
    website.setOrganization(Organization.builder().id(1L).build());
    websiteService.create(website);
  }
}
