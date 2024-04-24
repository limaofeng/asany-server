package cn.asany.shanhai.data.domain.converter;

import cn.asany.shanhai.data.domain.toy.DataSetFilter;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.AttributeConverter;
import java.util.List;
import net.asany.jfantasy.framework.jackson.JSON;
import net.asany.jfantasy.framework.util.common.StringUtil;

/**
 * 数据集 条件过滤
 *
 * @author limaofeng
 */
public class DataSetFilterArrayConverter
    implements AttributeConverter<List<DataSetFilter>, String> {
  @Override
  public String convertToDatabaseColumn(List<DataSetFilter> attribute) {
    if (attribute == null) {
      return null;
    }
    return JSON.serialize(attribute);
  }

  @Override
  public List<DataSetFilter> convertToEntityAttribute(String dbData) {
    if (StringUtil.isBlank(dbData)) {
      return null;
    }
    return JSON.deserialize(dbData, new TypeReference<List<DataSetFilter>>() {});
  }
}
