package cn.asany.cms.learn.bean.databind;

import cn.asany.cms.learn.bean.LearnerScope;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class LearnerScopeDeserializer extends JsonDeserializer<LearnerScope> {

  @Override
  public LearnerScope deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return LearnerScope.builder().scope(p.getValueAsString()).build();
  }
}
