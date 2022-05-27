package cn.asany.shanhai.core.domain.converter;

import java.util.Arrays;
import javax.persistence.AttributeConverter;
import org.jfantasy.framework.dao.jpa.PropertyFilter.MatchType;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;

public class MatchTypeConverter implements AttributeConverter<MatchType[], String> {

  @Override
  public String convertToDatabaseColumn(MatchType[] attribute) {
    if (attribute == null) {
      return null;
    }
    return JSON.serialize(Arrays.stream(attribute).map(item -> item.name()).toArray(String[]::new));
  }

  @Override
  @SuppressWarnings("unchecked")
  public MatchType[] convertToEntityAttribute(String dbData) {
    if (StringUtil.isBlank(dbData)) {
      return null;
    }
    return Arrays.stream(JSON.deserialize(dbData, new String[0]))
        .map(item -> MatchType.valueOf(item))
        .toArray(MatchType[]::new);
  }
}
