package cn.asany.myjob.factory.listener;

import cn.asany.myjob.factory.domain.Screen;
import cn.asany.myjob.factory.graphql.subscription.ScreenChangePublisher;
import cn.asany.myjob.factory.listener.event.ScreenChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ScreenChangeListener implements ApplicationListener<ScreenChangeEvent> {

  private final ScreenChangePublisher screenChangePublisher;

  public ScreenChangeListener(ScreenChangePublisher screenChangePublisher) {
    this.screenChangePublisher = screenChangePublisher;
  }

  @Override
  public void onApplicationEvent(ScreenChangeEvent event) {
    Screen screen = (Screen) event.getSource();
    screenChangePublisher.emit(screen);
  }
}
