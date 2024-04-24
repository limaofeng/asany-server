package cn.asany.shanhai.core.domain.converter;

import jakarta.persistence.AttributeConverter;
import java.util.Arrays;
import jakarta.persistence .AttributeConverter;
import net.asany.jfantasy.framework.dao.MatchType;
import net.asany.jfantasy.framework.jackson.JSON;
import net.asany.jfantasy.framework.util.common.StringUtil;

public class MatchTypeConverter implements AttributeConverter<MatchType[], String> {

  @Override
  public String convertToDatabaseColumn(MatchType[] attribute) {
    if (attribute == null) {
      return null;
    }
    return JSON.serialize(Arrays.stream(attribute).map(Enum::name).toArray(String[]::new));
  }

  @Override
  public MatchType[] convertToEntityAttribute(String dbData) {
    if (StringUtil.isBlank(dbData)) {
      return null;
    }
    return Arrays.stream(JSON.deserialize(dbData, new String[0]))
        .map(MatchType::valueOf)
        .toArray(MatchType[]::new);
  }
}
