package cn.asany.drive.service;

import cn.asany.drive.TestApplication;
import cn.asany.drive.bean.CloudDrive;
import java.util.List;
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
class CloudDriveServiceTest {

  @Autowired private CloudDriveService cloudDriveService;

  @Test
  void cloudDrives() {
    List<CloudDrive> cloudDrives = cloudDriveService.cloudDrives(1L);
    assert !cloudDrives.isEmpty();
  }

  @Test
  void createPersonalCloudDrive() {
    this.cloudDriveService.createPersonalCloudDrive(1L, 1024);
  }

  @Test
  void deleteCloudDrive() {
    this.cloudDriveService.deleteCloudDrive(1251L);
  }
}
