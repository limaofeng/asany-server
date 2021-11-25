package cn.asany.cms.learn.bean.databind;

import cn.asany.cms.learn.bean.Lesson;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class LessonDeserializer extends JsonDeserializer<Lesson> {

  @Override
  public Lesson deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return Lesson.builder().id(p.getValueAsLong()).build();
  }
}