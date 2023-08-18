package cn.asany.storage.data.service;

import cn.asany.base.utils.UUID;
import cn.asany.storage.TestApplication;
import cn.asany.storage.api.Storage;
import cn.asany.storage.data.domain.FileDetail;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
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
  void createDefaultAvatarsStorageSpace() {
    fileService.createStorageSpace(
        UUID.getShortId(),
        "用户默认图片",
        "/System/Library/UserManagement/DefaultAvatars/",
        Storage.DEFAULT_STORAGE_ID);
  }

  @Test
  void createStorageSpace() {
    fileService.createStorageSpace(
        UUID.getShortId(), "门店", "/modules/landing/", Storage.DEFAULT_STORAGE_ID);
  }

  @Test
  void createModulesStorageSpace() {
    fileService.createStorageSpace(
        UUID.getShortId(), "Modules", "/Applications/Modules", Storage.DEFAULT_STORAGE_ID);
  }

  @Test
  void createCmsStorageSpace() {
    fileService.createStorageSpace(
        UUID.getShortId(), "1", "/Applications/Website/1", Storage.DEFAULT_STORAGE_ID);
  }

  @Test
  void deleteStorageSpace() {
    fileService.deleteStorageSpace("757c1a816e9948afb7e60e1a7e7a067e");
  }

  @Test
  void repairData() {
    List<FileDetail> fileDetailList =
        fileService.findAll(
            PropertyFilter.newFilter().equal("extension", "").equal("isDirectory", false));
    for (FileDetail item : fileDetailList) {
      if (item.getMimeType().startsWith("image/")) {
        String extension = item.getMimeType().substring(item.getMimeType().lastIndexOf("/") + 1);
        item.setExtension(extension);
      }
      fileService.update(item.getId(), item, false);
    }
  }

  @Test
  void createFolder() {
    fileService.createFolder("/Applications/CloudDrive/limaofeng/");
  }
}
