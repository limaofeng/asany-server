package cn.asany.storage.data.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 初始化存储
 *
 * @author limaofeng
 */
@Component("storage.runners.init")
@Profile("!test")
@Slf4j
public class InitCommandLineRunner implements CommandLineRunner {

  @Override
  public void run(String... args) throws Exception {}
}
