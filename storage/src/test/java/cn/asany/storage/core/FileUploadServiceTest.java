package cn.asany.storage.core;

import cn.asany.storage.TestApplication;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.UploadException;
import cn.asany.storage.api.UploadOptions;
import cn.asany.storage.core.engine.virtual.VirtualFileObject;
import cn.asany.storage.data.util.IdUtils;
import cn.asany.storage.dto.SimpleFileObject;
import cn.asany.storage.utils.UploadUtils;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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
class FileUploadServiceTest {

  @Autowired private FileUploadService uploadService;

  private final String AVATARS_DIR =
      "/Users/limaofeng/Downloads/metronic_v8.1.1_html_demo7/demo7/dist/assets/media/svg/avatars";

  @BeforeEach
  void setUp() {}

  @Test
  void fileId() {
    log.debug(IdUtils.toKey("space", "Pi0jHf8Z", 21519L));
  }

  @Test
  void upload() throws UploadException {
    File dir = new File(AVATARS_DIR);

    String SPACE_ID = "Pi0jHf8Z";

    File[] files = dir.listFiles();
    assert files != null;
    for (File file : files) {
      UploadOptions options = UploadOptions.builder().space(SPACE_ID).build();
      VirtualFileObject uploadFile =
          (VirtualFileObject) uploadService.upload(UploadUtils.fileToObject(file), options);
      FileObject fileObject =
          SimpleFileObject.builder()
              .id(IdUtils.toKey("space", SPACE_ID, uploadFile.getId()))
              .directory(false)
              .name(uploadFile.getName())
              .size(uploadFile.getSize())
              .lastModified(uploadFile.getLastModified())
              .path(uploadFile.getOriginalPath())
              .build();
      log.debug(fileObject.getPath());
    }
  }
}
