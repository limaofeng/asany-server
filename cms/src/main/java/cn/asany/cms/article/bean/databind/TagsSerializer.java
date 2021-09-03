package cn.asany.cms.article.bean.databind;

import cn.asany.cms.article.bean.ArticleTag;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;

public class TagsSerializer extends JsonSerializer<List<ArticleTag>> {

  @Override
  public void serialize(List<ArticleTag> tags, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    gen.writeStartArray();
    for (ArticleTag tag : tags) {
      gen.writeString(tag.getName());
    }
    gen.writeEndArray();
  }
}
