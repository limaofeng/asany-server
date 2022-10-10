package cn.asany.shanhai.core.event;

import cn.asany.shanhai.core.domain.Model;
import org.springframework.context.ApplicationEvent;

public class DeleteModelEvent extends ApplicationEvent {
  public DeleteModelEvent(Model model) {
    super(model);
  }
}
