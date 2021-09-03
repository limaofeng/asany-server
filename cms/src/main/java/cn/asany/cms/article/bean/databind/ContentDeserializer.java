package cn.asany.cms.article.bean.databind;

import cn.asany.cms.article.bean.Content;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class ContentDeserializer extends JsonDeserializer<Content> {

  @Override
  public Content deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    return Content.builder().text(jp.getValueAsString()).build();
  }
}
