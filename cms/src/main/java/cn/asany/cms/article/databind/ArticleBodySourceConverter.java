package cn.asany.cms.article.databind;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.util.LinkedHashMap;
import org.jfantasy.framework.jackson.JSON;

public class ArticleBodySourceConverter
    extends StdConverter<LinkedHashMap<String, Object>, String> {
  @Override
  public String convert(LinkedHashMap<String, Object> value) {
    return JSON.serialize(value);
  }
}
