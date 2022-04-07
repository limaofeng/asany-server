package cn.asany.storage.data.service;

import cn.asany.storage.TestApplication;
import cn.asany.storage.api.Storage;
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
class ThumbnailServiceTest {

  @Autowired private FileService fileService;

  @Test
  void createStorageSpace() {
    this.fileService.createStorageSpace(
        ThumbnailService.THUMBNAIL_STORAGE_SPACE_KEY,
        "缩略图存放目录",
        "/.cache/thumbnail",
        Storage.DEFAULT_STORAGE_ID);
  }

  @Test
  void save() {}
}
