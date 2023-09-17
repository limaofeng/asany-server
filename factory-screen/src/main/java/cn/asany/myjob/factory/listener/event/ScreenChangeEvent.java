package cn.asany.myjob.factory.listener.event;

import cn.asany.myjob.factory.domain.Screen;
import java.time.Clock;
import org.springframework.context.ApplicationEvent;

public class ScreenChangeEvent extends ApplicationEvent {
  public ScreenChangeEvent(Screen screen) {
    super(screen, parseClock(screen));
  }

  private static Clock parseClock(Screen screen) {
    Clock clock = Clock.systemDefaultZone();
    return Clock.fixed(screen.getUpdatedAt().toInstant(), clock.getZone());
  }
}
