package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.domain.*;
import cn.asany.shanhai.core.domain.enums.ModelEndpointType;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.model.CustomModelService;
import cn.asany.shanhai.core.support.model.DefaultCustomModelService;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import cn.asany.shanhai.core.support.tools.DynamicClassGenerator;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.language.*;
import java.util.ArrayList;
import lombok.SneakyThrows;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;

public class GraphQLTypeUtils {

  private static FieldTypeRegistry fieldTypeRegistry;
  private static DynamicClassGenerator dynamicClassGenerator;

  private static FieldTypeRegistry getFieldTypeRegistry() {
    if (fieldTypeRegistry == null) {
      return fieldTypeRegistry = SpringBeanUtils.getBeanByType(FieldTypeRegistry.class);
    }
    return fieldTypeRegistry;
  }

  private static DynamicClassGenerator getDynamicClassGenerator() {
    if (dynamicClassGenerator == null) {
      return dynamicClassGenerator = SpringBeanUtils.getBeanByType(DynamicClassGenerator.class);
    }
    return dynamicClassGenerator;
  }

  public static Value<?> toDefaultValue(ModelEndpointArgument argument) {
    if (StringUtil.isBlank(argument.getDefaultValue())) {
      return null;
    }
    if ("{}".equals(argument.getDefaultValue())) {
      return ObjectValue.newObjectValue().build();
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

  public static Definition<?> makeObjectTypeDefinition(Model type) {
    ObjectTypeDefinition.Builder subTypeBuilder =
        ObjectTypeDefinition.newObjectTypeDefinition().name(type.getCode());
    for (ModelField field :
        ObjectUtil.sort(
            new ArrayList<>(type.getFields()),
            (l, r) -> {
              int lIndex =
                  l.getSystem()
                      ? (l.getPrimaryKey() ? l.getSort() : 10000 + l.getSort())
                      : 1000 + l.getSort();
              int rIndex =
                  r.getSystem()
                      ? (r.getPrimaryKey() ? r.getSort() : 10000 + r.getSort())
                      : 1000 + r.getSort();
              return Integer.compare(lIndex, rIndex);
            })) {
      subTypeBuilder.fieldDefinition(
          new FieldDefinition(
              field.getCode(),
              getType(
                  field.getType(), field.getRequired(), field.getList(), getFieldTypeRegistry())));
    }
    return subTypeBuilder.build();
  }

  public static Type<?> getType(
      String typeStr, boolean required, boolean list, FieldTypeRegistry fieldTypeRegistry) {
    FieldType<?, ?> fieldType = fieldTypeRegistry.getType(typeStr);

    Type<?> type = new TypeName(fieldType.getGraphQLType());

    type = required ? NonNullType.newNonNullType(type).build() : type;

    if (list) {
      type = ListType.newListType(type).build();
      type = required ? NonNullType.newNonNullType(type).build() : type;
    }

    return type;
  }

  public static Definition<?> makeInputTypeDefinition(Model model) {
    InputObjectTypeDefinition.Builder inputBuilder =
        InputObjectTypeDefinition.newInputObjectDefinition().name(model.getCode());

    if (model.getFields().isEmpty()) {
      inputBuilder.inputValueDefinition(
          InputValueDefinition.newInputValueDefinition()
              .name("_")
              .description(new Description("未配置如何输入字段", new SourceLocation(0, 0), true))
              .type(new TypeName("String"))
              .build());
    } else {
      for (ModelField field : model.getFields()) {
        inputBuilder.inputValueDefinition(
            InputValueDefinition.newInputValueDefinition()
                .name(field.getCode())
                .description(GraphQLTypeUtils.toDescription(field))
                .type(
                    getType(
                        field.getType(),
                        field.getRequired(),
                        field.getList(),
                        getFieldTypeRegistry()))
                .build());
      }
    }
    return inputBuilder.build();
  }

  public static FieldDefinition makeQueryFieldDefinition(ModelEndpoint endpoint) {
    return makeFieldDefinition(endpoint);
  }

  private static FieldDefinition makeFieldDefinition(ModelEndpoint endpoint) {
    ModelEndpointReturnType returnType = endpoint.getReturnType();
    FieldDefinition.Builder fieldBuilder =
        FieldDefinition.newFieldDefinition()
            .name(endpoint.getCode())
            .type(
                getType(
                    returnType.getType().getCode(),
                    returnType.getRequired(),
                    returnType.getList(),
                    getFieldTypeRegistry()))
            .description(GraphQLTypeUtils.toDescription(endpoint));
    for (ModelEndpointArgument argument :
        ObjectUtil.sort(new ArrayList<>(endpoint.getArguments()), "index", "asc")) {
      fieldBuilder.inputValueDefinition(
          InputValueDefinition.newInputValueDefinition()
              .name(argument.getName())
              .description(GraphQLTypeUtils.toDescription(argument))
              .defaultValue(GraphQLTypeUtils.toDefaultValue(argument))
              .type(
                  getType(
                      argument.getType(),
                      argument.getRequired(),
                      argument.getList(),
                      getFieldTypeRegistry()))
              .build());
    }
    return fieldBuilder.build();
  }

  public static FieldDefinition makeMutationFieldDefinition(ModelEndpoint endpoint) {
    return makeFieldDefinition(endpoint);
  }

  @SneakyThrows
  public static GraphQLResolver<?> makeQueryResolver(ModelRepository repository, Model model) {
    String namespace = model.getModule().getCode();

    ModelEndpoint listName = ObjectUtil.find(model.getEndpoints(), "type", ModelEndpointType.LIST);
    ModelEndpoint connectionName =
        ObjectUtil.find(model.getEndpoints(), "type", ModelEndpointType.CONNECTION);
    ModelEndpoint getName = ObjectUtil.find(model.getEndpoints(), "type", ModelEndpointType.GET);

    Class<GraphQLResolver<?>> resolverClass =
        getDynamicClassGenerator()
            .makeQueryResolver(
                namespace,
                model.getCode(),
                getName.getCode(),
                listName.getCode(),
                connectionName.getCode());

    return resolverClass
        .getConstructor(CustomModelService.class)
        .newInstance(new DefaultCustomModelService(repository));
  }

  @SneakyThrows
  public static GraphQLResolver<?> makeMutationResolver(ModelRepository repository, Model model) {
    String namespace = model.getModule().getCode();

    ModelEndpoint create = ObjectUtil.find(model.getEndpoints(), "type", ModelEndpointType.CREATE);
    ModelEndpoint update = ObjectUtil.find(model.getEndpoints(), "type", ModelEndpointType.UPDATE);
    ModelEndpoint delete = ObjectUtil.find(model.getEndpoints(), "type", ModelEndpointType.DELETE);
    ModelEndpoint deleteMany =
        ObjectUtil.find(model.getEndpoints(), "type", ModelEndpointType.DELETE_MANY);

    Class<GraphQLResolver<?>> resolverClass =
        getDynamicClassGenerator()
            .makeMutationResolver(
                namespace,
                model.getCode(),
                create.getCode(),
                update.getCode(),
                delete.getCode(),
                deleteMany.getCode());

    return resolverClass
        .getConstructor(CustomModelService.class)
        .newInstance(new DefaultCustomModelService(repository));
  }

  public static Definition<?> makeEnumTypeDefinition(Model type) {
    EnumTypeDefinition.Builder subTypeBuilder =
        EnumTypeDefinition.newEnumTypeDefinition().name(type.getCode());
    for (ModelField field : type.getFields()) {
      subTypeBuilder.enumValueDefinition(
          EnumValueDefinition.newEnumValueDefinition().name(field.getCode()).build());
    }
    return subTypeBuilder.build();
  }
}
