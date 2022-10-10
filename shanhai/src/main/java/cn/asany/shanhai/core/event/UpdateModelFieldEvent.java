package cn.asany.shanhai.core.event;

import cn.asany.shanhai.core.domain.ModelField;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

public class UpdateModelFieldEvent extends ApplicationEvent {

  public UpdateModelFieldEvent(Long modelId, ModelField field) {
    super(new UpdateModelFieldEventSource(modelId, field));
  }

  @Data
  @AllArgsConstructor
  public static class UpdateModelFieldEventSource {
    private Long modelId;
    private ModelField field;
  }
}
