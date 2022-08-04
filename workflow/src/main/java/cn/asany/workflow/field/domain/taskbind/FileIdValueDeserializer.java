package cn.asany.workflow.field.domain.taskbind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import cn.asany.pm.field.bean.IssueField;

public class FileIdValueDeserializer extends JsonDeserializer<IssueField> {
  @Override
  public IssueField deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    Long id = p.getValueAsLong();
    return IssueField.builder().id(id).build();
  }
}
