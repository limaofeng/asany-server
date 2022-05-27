package cn.asany.cms.learn.domain.databind;

import cn.asany.cms.learn.domain.Learner;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class LearnerDeserializer extends JsonDeserializer<Learner> {

  @Override
  public Learner deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return Learner.builder().id(p.getValueAsLong()).build();
  }
}
