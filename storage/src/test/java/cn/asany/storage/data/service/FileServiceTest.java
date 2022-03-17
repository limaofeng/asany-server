package cn.asany.storage.data.service;

import cn.asany.base.utils.UUID;
import cn.asany.storage.TestApplication;
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
class FileServiceTest {

  @Autowired private FileService fileService;

  @Test
  void createStorageSpace() {
    fileService.createStorageSpace(UUID.getShortId(), "测试", "/test-001/", "Global");
  }

  @Test
  void deleteStorageSpace() {
    fileService.deleteStorageSpace("757c1a816e9948afb7e60e1a7e7a067e");
  }
}
