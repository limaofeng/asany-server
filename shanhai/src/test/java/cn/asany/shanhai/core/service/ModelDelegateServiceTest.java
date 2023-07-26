package cn.asany.shanhai.core.service;

import cn.asany.shanhai.TestApplication;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
class ModelDelegateServiceTest {

  //  private DefaultCRUDDelegateCommandLineRunner runner;

  @SneakyThrows
  @BeforeEach
  void setUp() {
    //    runner =
    //        SpringBeanUtils.createBean(
    //            DefaultCRUDDelegateCommandLineRunner.class,
    // SpringBeanUtils.AutoType.AUTOWIRE_BY_TYPE);
  }

  @Test
  @SneakyThrows
  void save() {
    //    runner.run(new String[0]);
  }
}
