package cn.asany.workflow.field.domain.taskbind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import cn.asany.pm.issue.bean.Issue;

public class IssueValueDeserializer extends JsonDeserializer<Issue> {
  @Override
  public Issue deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    Long id = p.getValueAsLong();
    return Issue.builder().id(id).build();
  }
}
