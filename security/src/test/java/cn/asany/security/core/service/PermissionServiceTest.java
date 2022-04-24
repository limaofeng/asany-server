package cn.asany.security.core.service;

import cn.asany.TestApplication;
import cn.asany.security.core.bean.Permission;
import cn.asany.security.core.service.dto.ImportPermission;
import java.io.InputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.yaml.snakeyaml.Yaml;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PermissionServiceTest {

  @Autowired private PermissionService permissionService;

  @Test
  void importPermission() {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("permission.yml");
    // 调基础工具类的方法
    Yaml yaml = new Yaml();
    ImportPermission importPermission = yaml.loadAs(inputStream, ImportPermission.class);

    List<Permission> permissions = permissionService.importPermission(importPermission);

    log.debug(String.format("导入权限成功,  共 %d 条数据", permissions.size()));
  }
}
