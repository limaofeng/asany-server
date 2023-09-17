package cn.asany.myjob.factory.listener.event;

import cn.asany.myjob.factory.domain.Screen;
import java.time.Clock;
import org.springframework.context.ApplicationEvent;

public class ScreenCreateEvent extends ApplicationEvent {
  public ScreenCreateEvent(Screen screen) {
    super(screen, parseClock(screen));
  }

  private static Clock parseClock(Screen screen) {
    Clock clock = Clock.systemDefaultZone();
    return Clock.fixed(screen.getCreatedAt().toInstant(), clock.getZone());
  }
}
