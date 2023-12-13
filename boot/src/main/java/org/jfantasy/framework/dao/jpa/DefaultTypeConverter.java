package org.jfantasy.framework.dao.jpa;

import org.jfantasy.framework.dao.hibernate.util.ReflectionUtils;

public class DefaultTypeConverter<T> implements TypeConverter<T> {

  private final Class<T> type;

  public DefaultTypeConverter(Class<T> type) {
    this.type = type;
  }

  @Override
  public T convert(Object value) {
    if (type.isAssignableFrom(value.getClass())) {
      return type.cast(value);
    }
    if (type.isEnum()) {
      //noinspection rawtypes,unchecked
      return (T) Enum.valueOf((Class<Enum>) type, value.toString());
    }
    return ReflectionUtils.convert(value, type);
  }
}
