package cn.asany.ui.resources.domain.converter;

import cn.asany.ui.resources.domain.toy.ComponentData;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.List;
import net.asany.jfantasy.framework.jackson.JSON;

public class ComponentDataConverter implements AttributeConverter<List<ComponentData>, String> {

  @Override
  public String convertToDatabaseColumn(List<ComponentData> attribute) {
    return JSON.serialize(attribute);
  }

  @Override
  public List<ComponentData> convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return new ArrayList<>();
    }
    return JSON.deserialize(dbData, new TypeReference<List<ComponentData>>() {});
  }
}
