package cn.asany.storage.data.domain.converter;

import cn.asany.storage.data.domain.FileDetail;
import jakarta.persistence.AttributeConverter;
import net.asany.jfantasy.framework.jackson.JSON;
import net.asany.jfantasy.framework.util.common.StringUtil;

public class FileDetailConverter implements AttributeConverter<FileDetail, String> {

  @Override
  public String convertToDatabaseColumn(FileDetail attribute) {
    if (attribute == null) {
      return null;
    }
    return JSON.serialize(attribute);
  }

  @Override
  public FileDetail convertToEntityAttribute(String dbData) {
    if (StringUtil.isBlank(dbData)) {
      return null;
    }
    return JSON.deserialize(dbData, FileDetail.class);
  }
}
