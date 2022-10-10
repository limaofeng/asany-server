package cn.asany.shanhai.core.event;

import cn.asany.shanhai.core.domain.Model;
import org.springframework.context.ApplicationEvent;

public class CreateModelEvent extends ApplicationEvent {

  public CreateModelEvent(Model model) {
    super(model);
  }
}
