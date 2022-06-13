package cn.asany.cms.article.domain.converter;

import cn.asany.cms.article.domain.ArticleMetadata;
import javax.persistence.AttributeConverter;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;

public class MetaDataConverter implements AttributeConverter<ArticleMetadata, String> {
  @Override
  public String convertToDatabaseColumn(ArticleMetadata attribute) {
    if (attribute == null) {
      return null;
    }
    return JSON.serialize(attribute);
  }

  @Override
  public ArticleMetadata convertToEntityAttribute(String dbData) {
    if (StringUtil.isBlank(dbData)) {
      return new ArticleMetadata();
    }
    return JSON.deserialize(dbData, ArticleMetadata.class);
  }
}
