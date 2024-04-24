package cn.asany.storage.data.domain.converter;

import cn.asany.storage.data.domain.Image;
import jakarta.persistence.AttributeConverter;
import net.asany.jfantasy.framework.jackson.JSON;
import net.asany.jfantasy.framework.util.common.StringUtil;

public class ImagesConverter implements AttributeConverter<Image[], String> {

  @Override
  public String convertToDatabaseColumn(Image[] attribute) {
    if (attribute == null) {
      return null;
    }
    return JSON.serialize(attribute);
  }

  @Override
  public Image[] convertToEntityAttribute(String dbData) {
    if (StringUtil.isBlank(dbData)) {
      return null;
    }
    return JSON.deserialize(dbData, Image[].class);
  }
}
