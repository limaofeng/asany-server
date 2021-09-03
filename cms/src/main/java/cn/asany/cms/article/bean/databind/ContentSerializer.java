package cn.asany.cms.article.bean.databind;

import cn.asany.cms.article.bean.Content;
import cn.asany.cms.article.bean.enums.ContentType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class ContentSerializer extends JsonSerializer<Content> {

  @Override
  public void serialize(Content value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    if (ContentType.html.equals(value.getType())) {
      gen.writeString(value.getText());
    }
  }
}
