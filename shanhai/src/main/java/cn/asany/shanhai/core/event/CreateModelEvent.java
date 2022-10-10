package cn.asany.shanhai.core.event;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelField;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

public class CreateModelEvent extends ApplicationEvent {

  public CreateModelEvent(Model model) {
    super(model);
  }
}
