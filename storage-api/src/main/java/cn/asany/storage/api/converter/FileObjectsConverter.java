package cn.asany.storage.api.converter;

import cn.asany.storage.api.FileObject;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;

import javax.persistence.AttributeConverter;
import java.util.List;

/**
 * 多个文件
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-04-09 18:17
 */
public class FileObjectsConverter implements AttributeConverter<List<FileObject>, String> {

  @Override
  public String convertToDatabaseColumn(List<FileObject> attribute) {
    if (attribute == null) {
      return null;
    }
    return JSON.serialize(attribute);
  }

  @Override
  public List<FileObject> convertToEntityAttribute(String dbData) {
    if (StringUtil.isBlank(dbData)) {
      return null;
    }
    return JSON.deserialize(dbData, new TypeReference<List<FileObject>>() {});
  }
}
