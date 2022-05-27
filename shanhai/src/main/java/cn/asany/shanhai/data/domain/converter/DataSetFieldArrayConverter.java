package cn.asany.shanhai.data.domain.converter;

import cn.asany.shanhai.data.domain.toy.DataSetField;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import javax.persistence.AttributeConverter;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;

/**
 * 数据集字段
 *
 * @author limaofeng
 */
public class DataSetFieldArrayConverter implements AttributeConverter<List<DataSetField>, String> {
  @Override
  public String convertToDatabaseColumn(List<DataSetField> attribute) {
    if (attribute == null) {
      return null;
    }
    return JSON.serialize(attribute);
  }

  @Override
  public List<DataSetField> convertToEntityAttribute(String dbData) {
    if (StringUtil.isBlank(dbData)) {
      return null;
    }
    return JSON.deserialize(dbData, new TypeReference<List<DataSetField>>() {});
  }
}
