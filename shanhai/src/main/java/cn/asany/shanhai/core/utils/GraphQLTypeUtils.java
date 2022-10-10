package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.domain.ModelEndpoint;
import cn.asany.shanhai.core.domain.ModelEndpointArgument;
import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import graphql.language.*;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.framework.util.common.StringUtil;

public class GraphQLTypeUtils {

  private static FieldTypeRegistry fieldTypeRegistry;

  private static FieldTypeRegistry getFieldTypeRegistry() {
    if (fieldTypeRegistry == null) {
      return fieldTypeRegistry = SpringBeanUtils.getBeanByType(FieldTypeRegistry.class);
    }
    return fieldTypeRegistry;
  }

  public static Value<?> toDefaultValue(ModelEndpointArgument argument) {
    if (StringUtil.isBlank(argument.getDefaultValue())) {
      return null;
    }
    FieldType<?, ?> fieldType = getFieldTypeRegistry().getType(argument.getType());
    switch (fieldType.getGraphQLType()) {
      case "Int":
        return IntValue.of(Integer.parseInt(argument.getDefaultValue()));
      case "String":
        return StringValue.of(argument.getDefaultValue());
      case "Boolean":
        return BooleanValue.of(Boolean.parseBoolean(argument.getDefaultValue()));
      default:
        return null;
    }
  }

  public static Description toDescription(ModelEndpointArgument argument) {
    if (StringUtil.isBlank(argument.getDescription())) {
      return null;
    }
    return new Description(argument.getDescription(), new SourceLocation(0, 0), true);
  }

  public static Description toDescription(ModelEndpoint endpoint) {
    return new Description(
        endpoint.getName() + "\n" + StringUtil.defaultValue(endpoint.getDescription(), ""),
        new SourceLocation(0, 0),
        true);
  }

  public static Description toDescription(ModelField field) {
    return new Description(
        field.getName() + "\n" + StringUtil.defaultValue(field.getDescription(), ""),
        new SourceLocation(0, 0),
        true);
  }
}
