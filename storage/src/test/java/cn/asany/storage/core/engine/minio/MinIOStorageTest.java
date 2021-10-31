package cn.asany.storage.core.engine.minio;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MinIOStorageTest {

  private Storage storage;

  @BeforeEach
  void setUp() {
    storage =
        new MinIOStorage(
            "test",
            "aliyun-oss.minio.thuni-h.com",
            "LTAI4FbseyNZGo6xPn5GWojd",
            "kOEOk421FxzjoGt8y5ysK8zMoGrX6c",
            "whir-storage",
            true);
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
    object.listFiles();
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
    FileObject root = storage.getFileItem("/");
    System.out.println(root.lastModified());
  }

  @Test
  void removeFile() {}
}
