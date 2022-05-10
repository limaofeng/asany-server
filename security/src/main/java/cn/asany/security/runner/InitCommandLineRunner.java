package cn.asany.security.runner;

import lombok.extern.slf4j.Slf4j;
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

  @Override
  public void run(String... args) {}
}
