package cn.asany.storage.data.service;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.storage.TestApplication;
import cn.asany.storage.data.bean.Space;
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
class SpaceServiceTest {

  @Autowired private SpaceService spaceService;

  @Test
  void save() {
    this.spaceService.save(
        Space.builder()
            .id("Default")
            //            .storage(StorageConfig.builder().id("Global").build())
            .name("默认")
            //            .path("/temp")
            .build());
  }
}
