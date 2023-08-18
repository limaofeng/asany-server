package cn.asany.security.core.service;

import cn.asany.TestApplication;
import cn.asany.security.core.domain.PermissionPolicy;
import cn.asany.security.core.domain.PermissionStatement;
import cn.asany.security.core.service.dto.ImportPermission;
import cn.asany.security.core.service.dto.ImportPermissionPolicies;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PermissionPolicyServiceTest {

  @Autowired private PermissionPolicyService permissionPolicyService;

  @Test
  void importPermission() {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("permission.yml");
    // 调基础工具类的方法
    Yaml yaml = new Yaml();
    ImportPermission importPermission = yaml.loadAs(inputStream, ImportPermission.class);

    List<PermissionStatement> permissions =
        new ArrayList<>(); // permissionService.importPermission(importPermission);

    log.debug(String.format("导入权限成功,  共 %d 条数据", permissions.size()));
  }

  @Test
  void systemPermissionPolicy() {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("system-permission-policy.yml");

    Constructor constructor = new Constructor(ImportPermissionPolicies.class);
    TypeDescription moduleTypeDescription = new StatementActionTypeDescription();
    constructor.addTypeDescription(moduleTypeDescription);
    Yaml yaml = new Yaml(constructor);

    ImportPermissionPolicies permissionPolicies =
        yaml.loadAs(inputStream, ImportPermissionPolicies.class);

    List<PermissionPolicy> policies = permissionPolicies.getPolicies();
    log.debug(String.format("读取到 %d 条权限策略数据", policies.size()));

    permissionPolicyService.importPermissionPolicies(policies);
  }

  public static class StatementActionTypeDescription extends TypeDescription {

    public StatementActionTypeDescription() {
      super(String[].class);
    }

    @Override
    public Object newInstance(Node node) {
      String value = ((ScalarNode) node).getValue();
      return new String[] {value};
    }
  }
}
