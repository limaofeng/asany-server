package org.jfantasy.framework.dao.hibernate.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import org.jfantasy.framework.dao.hibernate.util.ReflectionUtils;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.StringUtil;

import javax.persistence.AttributeConverter;
import java.util.Set;

public class SetConverter<T> implements AttributeConverter<Set<T>, String> {

    protected Class entityClass;

    public SetConverter() {
        this.entityClass = ReflectionUtils.getSuperClassGenricType(ClassUtil.getRealClass(getClass())).getComponentType();
    }

    @Override
    public String convertToDatabaseColumn(Set<T> attribute) {
        if (attribute == null) {
            return null;
        }
        return JSON.serialize(attribute);
    }

    @Override
    public Set<T> convertToEntityAttribute(String dbData) {
        if (StringUtil.isBlank(dbData)) {
            return null;
        }
        return JSON.deserialize(dbData, new TypeReference<Set<T>>() {
        });
    }

}