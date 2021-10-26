package cn.asany.storage.data.service;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.storage.TestApplication;
import cn.asany.storage.core.engine.oss.OSSStorageConfig;
import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.bean.enums.StorageType;
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
class StorageServiceTest {

  @Autowired private StorageService storageService;

  @Test
  void save() {
    StorageConfig config =
        StorageConfig.builder()
            .id("Global")
            .name("全局")
            .type(
                StorageType.OSS,
                OSSStorageConfig.builder()
                    .endpoint("oss-cn-hangzhou.aliyuncs.com")
                    .accessKeyId("LTAI0Vj7YKUQILET")
                    .accessKeySecret("GLWV14v8ndv5UbudpglGFl16K8QWBX")
                    .bucketName("static-jfantasy-org")
                    .build())
            .build();
    this.storageService.save(config);
  }
}
