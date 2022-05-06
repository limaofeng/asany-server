package cn.asany.security.runner;

import cn.asany.security.core.bean.Role;
import cn.asany.security.core.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 数据初始化 <br>
 * 生成默认角色
 *
 * @author limaofeng
 */
@Component("security.InitCommandLineRunner")
@Profile("!test")
@Slf4j
public class InitCommandLineRunner implements CommandLineRunner {

  @Autowired private RoleService roleService;

  @Override
  public void run(String... args) {
    if (!this.roleService.findById(Role.USER.getId()).isPresent()) {
      this.roleService.save(Role.USER);
    }
    if (!this.roleService.findById(Role.ADMIN.getId()).isPresent()) {
      this.roleService.save(Role.ADMIN);
    }
  }
}
