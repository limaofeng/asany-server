package cn.asany.cms.learn.bean.databind;

import cn.asany.cms.learn.bean.Course;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class CourseDeserializer extends JsonDeserializer<Course> {

  @Override
  public Course deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return Course.builder().id(p.getValueAsLong()).build();
  }
}
