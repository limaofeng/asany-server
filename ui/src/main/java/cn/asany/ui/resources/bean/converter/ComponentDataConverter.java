package cn.asany.ui.resources.bean.converter;

import cn.asany.ui.resources.bean.toy.ComponentData;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jfantasy.framework.jackson.JSON;

import javax.persistence.AttributeConverter;
import java.util.List;

public class ComponentDataConverter implements AttributeConverter<List<ComponentData>, String> {

    @Override
    public String convertToDatabaseColumn(List<ComponentData> attribute) {
        return JSON.serialize(attribute);
    }

    @Override
    public List<ComponentData> convertToEntityAttribute(String dbData) {
        return JSON.deserialize(dbData, new TypeReference<List<ComponentData>>() {
        });
    }

}
