package cn.asany.shanhai.core.event;

import cn.asany.shanhai.core.domain.ModelField;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

public class CreateModelFieldEvent extends ApplicationEvent {

  public CreateModelFieldEvent(Long modelId, ModelField field) {
    super(new CreateModelFieldSource(modelId, field));
  }

  @Data
  @AllArgsConstructor
  public static class CreateModelFieldSource {
    private Long modelId;
    private ModelField field;
  }
}
