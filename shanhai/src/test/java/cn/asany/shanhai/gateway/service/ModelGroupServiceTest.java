package cn.asany.shanhai.gateway.service;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.shanhai.TestApplication;
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
class ModelGroupServiceTest {

  @Autowired private ModelGroupService modelGroupService;

  @Test
  void autoGroup() {
    modelGroupService.autoGroup();
  }
}
