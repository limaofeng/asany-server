package cn.asany.cms.article.bean.converter;

import cn.asany.cms.article.bean.MetaData;
import javax.persistence.AttributeConverter;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;

public class MetaDataConverter implements AttributeConverter<MetaData, String> {
  @Override
  public String convertToDatabaseColumn(MetaData attribute) {
    if (attribute == null) {
      return null;
    }
    return JSON.serialize(attribute);
  }

  @Override
  public MetaData convertToEntityAttribute(String dbData) {
    if (StringUtil.isBlank(dbData)) {
      return new MetaData();
    }
    return JSON.deserialize(dbData, MetaData.class);
  }
}
