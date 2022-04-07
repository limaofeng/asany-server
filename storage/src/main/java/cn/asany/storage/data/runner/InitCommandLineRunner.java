package cn.asany.storage.data.runner;

import cn.asany.storage.data.job.GenerateThumbnailJob;
import cn.asany.storage.data.service.ThumbnailService;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.schedule.service.SchedulerUtil;
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

  private final SchedulerUtil schedulerUtil;

  public InitCommandLineRunner(SchedulerUtil schedulerUtil) {
    this.schedulerUtil = schedulerUtil;
  }

  @Override
  public void run(String... args) {
    if (!this.schedulerUtil.checkExists(ThumbnailService.JOBKEY_GENERATE_THUMBNAIL)) {
      this.schedulerUtil.addJob(
          ThumbnailService.JOBKEY_GENERATE_THUMBNAIL, GenerateThumbnailJob.class);
    }
  }
}
