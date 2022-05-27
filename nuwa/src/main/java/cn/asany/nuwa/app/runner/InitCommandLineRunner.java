package cn.asany.nuwa.app.runner;

import cn.asany.nuwa.app.domain.Routespace;
import cn.asany.nuwa.app.service.RoutespaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 初始化 路由空间
 *
 * @author limaofeng
 */
@Component("nuwa.app.InitCommandLineRunner")
@Profile("!test")
@Slf4j
public class InitCommandLineRunner implements CommandLineRunner {

  private final RoutespaceService routespaceService;

  public InitCommandLineRunner(RoutespaceService routespaceService) {
    this.routespaceService = routespaceService;
  }

  @Override
  public void run(String... args) {
    if (!routespaceService.findById(Routespace.DEFAULT_ROUTESPACE_WEB.getId()).isPresent()) {
      this.routespaceService.createRoutespace(Routespace.DEFAULT_ROUTESPACE_WEB);
    }
    if (!routespaceService.findById(Routespace.DEFAULT_ROUTESPACE_WAP.getId()).isPresent()) {
      this.routespaceService.createRoutespace(Routespace.DEFAULT_ROUTESPACE_WAP);
    }
  }
}
