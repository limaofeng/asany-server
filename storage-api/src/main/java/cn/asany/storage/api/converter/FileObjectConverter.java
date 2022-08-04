package cn.asany.storage.api.converter;

import cn.asany.storage.api.FileObject;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;

/**
 * 文件转换
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Converter
public class FileObjectConverter implements AttributeConverter<FileObject, String> {

  @Override
  public String convertToDatabaseColumn(FileObject attribute) {
    if (attribute == null) {
      return null;
    }
    return JSON.serialize(attribute);
  }

  @Override
  public FileObject convertToEntityAttribute(String dbData) {
    if (StringUtil.isBlank(dbData)) {
      return null;
    }
    return JSON.deserialize(dbData, FileObject.class);
  }
}
