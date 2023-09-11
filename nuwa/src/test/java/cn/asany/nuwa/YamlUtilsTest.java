package cn.asany.nuwa;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.nuwa.app.service.dto.NativeApplication;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class YamlUtilsTest {

  @Test
  void loadApp() throws IOException {
    String resourcePath = ClassLoader.getSystemResource(".").getPath();
    String projectPath = resourcePath.substring(0, resourcePath.lastIndexOf("nuwa"));
    InputStream inputStream =
        Files.newInputStream(Paths.get(projectPath, "boot/src/test/resources/app.yml"));
    // 调基础工具类的方法
    NativeApplication app = YamlUtils.load(inputStream);

    assertNotNull(app);
  }

  @Test
  void loadScreenApp() throws IOException {
    String resourcePath = ClassLoader.getSystemResource(".").getPath();
    String projectPath = resourcePath.substring(0, resourcePath.lastIndexOf("nuwa"));
    InputStream inputStream =
        Files.newInputStream(Paths.get(projectPath, "boot/src/test/resources/app_screen.yml"));
    // 调基础工具类的方法
    NativeApplication app = YamlUtils.load(inputStream);

    assertNotNull(app);
  }
}
