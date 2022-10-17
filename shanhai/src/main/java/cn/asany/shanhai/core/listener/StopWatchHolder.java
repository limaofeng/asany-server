package cn.asany.shanhai.core.listener;

import java.util.UUID;
import org.springframework.util.StopWatch;

public class StopWatchHolder {

  private static final ThreadLocal<StopWatch> STOP_WATCH = new ThreadLocal<>();

  public static StopWatch get() {
    StopWatch stopWatch = STOP_WATCH.get();
    if (stopWatch == null) {
      STOP_WATCH.set(new StopWatch(UUID.randomUUID().toString()));
      return STOP_WATCH.get();
    }
    return STOP_WATCH.get();
  }
}
