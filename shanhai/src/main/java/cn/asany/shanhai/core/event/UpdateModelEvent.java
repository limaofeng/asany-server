package cn.asany.shanhai.core.event;

import cn.asany.shanhai.core.domain.Model;
import org.springframework.context.ApplicationEvent;

public class UpdateModelEvent extends ApplicationEvent {
  public UpdateModelEvent(Model model) {
    super(model);
  }
}
