package cn.asany.storage.core.engine.minio;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.Storage;
import cn.asany.storage.core.engine.oss.OSSStorage;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class OSSStorageTest {

  private Storage storage;

  @BeforeEach
  void setUp() {
    storage =
        new OSSStorage(
            "test",
            "oss-cn-hangzhou.aliyuncs.com",
            "LTAI0Vj7YKUQILET",
            "GLWV14v8ndv5UbudpglGFl16K8QWBX",
            "static-jfantasy-org");
  }

  @Test
  void writeFile() {}

  @Test
  void testWriteFile() {}

  @Test
  void testWriteFile1() {}

  @Test
  void readFile() {}

  @Test
  void testReadFile() {}

  @Test
  void testReadFile1() {}

  @Test
  void listFiles() {
    FileObject object = storage.getFileItem("/");
    for (FileObject file : object.listFiles()) {
      log.debug("path: " + file.getPath());
    }
  }

  @Test
  void testListFiles() {}

  @Test
  void testListFiles1() {}

  @Test
  void testListFiles2() {}

  @Test
  void testListFiles3() {}

  @Test
  void testListFiles4() {}

  @Test
  void getFileItem() {
    FileObject fileObject = storage.getFileItem("/2015-04-05/");
    List<FileObject> parentFiles = new ArrayList<>();
    FileObject parentFile = fileObject.getParentFile();
    while (parentFile != null) {
      parentFiles.add(parentFile);
      log.debug("file: " + parentFile.getPath());
      parentFile = parentFile.getParentFile();
    }
    log.debug("files size: " + parentFiles.size());
  }

  @Test
  void removeFile() {}
}
