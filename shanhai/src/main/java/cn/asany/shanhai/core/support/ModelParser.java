package cn.asany.shanhai.core.support;

import cn.asany.shanhai.core.domain.*;
import cn.asany.shanhai.core.domain.enums.ModelConnectType;
import cn.asany.shanhai.core.domain.enums.ModelEndpointType;
import cn.asany.shanhai.core.domain.enums.ModelRelationType;
import cn.asany.shanhai.core.service.ModelEndpointService;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.dao.ModelSessionFactory;
import cn.asany.shanhai.core.support.graphql.ModelDataFetcher;
import cn.asany.shanhai.core.support.graphql.ModelDelegateFactory;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import graphql.language.*;
import graphql.schema.DataFetcher;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.util.asm.AsmUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class ModelParser {

  @Autowired FieldTypeRegistry fieldTypeRegistry;
  @Autowired private ModelService modelService;
  @Autowired private ModelEndpointService modelEndpointService;
  @Autowired private ModelSessionFactory modelSessionFactory;
  @Autowired private ModelDelegateFactory delegateFactory;
  private final Map<Long, ModelResource> modelResourceMap = new ConcurrentHashMap<>();
  private final Map<String, DataFetcher<Object>> dataFetcherMap = new ConcurrentHashMap<>();
  private final Map<String, Class<?>> beanClassMap = new ConcurrentHashMap<>();

  private final Map<String, Definition<?>> definitionMap = new ConcurrentHashMap<>();

  public List<Model> getModels() {
    return modelResourceMap.values().stream()
        .map(ModelResource::getModel)
        .collect(Collectors.toList());
  }

  public void init() {
    List<Model> models = this.modelService.findEntityModels();

    ObjectTypeExtensionDefinition.Builder queryBuilder =
        ObjectTypeExtensionDefinition.newObjectTypeExtensionDefinition().name("Query");

    for (Model m : models) {
      Model model = this.modelService.getDetails(m.getId());

      ModelRepository modelRepository = modelSessionFactory.buildModelRepository(model);

      this.modelResourceMap.put(
          model.getId(), ModelResource.builder().model(model).repository(modelRepository).build());

      ObjectTypeDefinition.Builder typeBuilder =
          ObjectTypeDefinition.newObjectTypeDefinition().name(model.getCode());
      for (ModelField field : model.getFields()) {
        Type type = getType(field.getType(), field.getUnique(), field.getList(), fieldTypeRegistry);
        typeBuilder.fieldDefinition(new FieldDefinition(field.getCode(), type));
      }

      // 生成主对象
      definitionMap.put(model.getCode(), typeBuilder.build());
      this.makeBeanClass(model.getModule().getCode(), model);

      // 检测依赖对象
      for (ModelRelation relation : model.getRelations()) {
        Model inverse = this.modelService.getDetails(relation.getInverse().getId());
        relation.setInverse(inverse);
        if (relation.getType() != ModelRelationType.SUBJECTION) {
          continue;
        }
        Model type = relation.getInverse();

        if (ModelConnectType.GRAPHQL_OBJECT_EDGE.name().equals(relation.getRelation())
            || ModelConnectType.GRAPHQL_OBJECT_CONNECTION.name().equals(relation.getRelation())) {
          // 生成的分页相关对象
          definitionMap.put(type.getCode(), this.makeObjectTypeDefinition(type));
          beanClassMap.put(type.getCode(), this.makeBeanClass(model.getModule().getCode(), type));

        } else if (ModelConnectType.GRAPHQL_ENUM_ORDER_BY.name().equals(relation.getRelation())) {
          // 生成排序枚举
          definitionMap.put(type.getCode(), this.makeEnumTypeDefinition(type));
          beanClassMap.put(type.getCode(), this.makeEnumClass(model.getModule().getCode(), type));
        } else if (ModelConnectType.GRAPHQL_INPUT_CREATE.name().equals(relation.getRelation())
            || ModelConnectType.GRAPHQL_INPUT_UPDATE.name().equals(relation.getRelation())
            || ModelConnectType.GRAPHQL_INPUT_WHERE.name().equals(relation.getRelation())) {
          // 生成输入对象
          InputObjectTypeDefinition.Builder inputBuilder =
              InputObjectTypeDefinition.newInputObjectDefinition();
          // TODO: xxx
        }
      }

      List<ModelEndpoint> endpoints = this.modelEndpointService.listEndpoints(model.getId());
      model.setEndpoints(new HashSet<>(endpoints));

      for (ModelEndpoint endpoint : endpoints) {
        // 生成接口的定义
        if (ModelEndpointType.LIST == endpoint.getType()
            || endpoint.getType() == ModelEndpointType.GET
            || endpoint.getType() == ModelEndpointType.CONNECTION) {
          ModelEndpointReturnType returnType = endpoint.getReturnType();
          FieldDefinition.Builder fieldBuilder =
              FieldDefinition.newFieldDefinition()
                  .name(endpoint.getCode())
                  .type(
                      getType(
                          returnType.getType().getCode(),
                          returnType.getRequired(),
                          returnType.getList(),
                          fieldTypeRegistry))
                  .description(
                      new Description(
                          endpoint.getName() + "<br/>" + endpoint.getDescription(),
                          new SourceLocation(0, 0),
                          true));
          for (ModelEndpointArgument argument : endpoint.getArguments()) {
            fieldBuilder.inputValueDefinition(
                InputValueDefinition.newInputValueDefinition()
                    .name(argument.getName())
                    .description(
                        new Description(
                            argument.getDescription() + "<br/>" + endpoint.getDescription(),
                            new SourceLocation(0, 0),
                            true))
                    .type(
                        getType(
                            argument.getType(),
                            argument.getRequired(),
                            argument.getList(),
                            fieldTypeRegistry))
                    .build());
          }
          queryBuilder.fieldDefinition(fieldBuilder.build());
        } else {

        }

        // 生成接口的 DataFetcher
        dataFetcherMap.put(
            model.getCode() + "." + endpoint.getCode(),
            new ModelDataFetcher(
                delegateFactory.build(model, endpoint, modelRepository, endpoint.getDelegate())));
      }
    }

    // 添加接口定义
    definitionMap.put("Query", queryBuilder.build());
  }

  private Definition<?> makeEnumTypeDefinition(Model type) {
    EnumTypeDefinition.Builder subTypeBuilder =
        EnumTypeDefinition.newEnumTypeDefinition().name(type.getCode());
    for (ModelField field : type.getFields()) {
      subTypeBuilder.enumValueDefinition(
          EnumValueDefinition.newEnumValueDefinition().name(field.getCode()).build());
    }
    return subTypeBuilder.build();
  }

  public Type getType(
      String typeStr, boolean unique, boolean list, FieldTypeRegistry fieldTypeRegistry) {
    FieldType fieldType = fieldTypeRegistry.getType(typeStr);

    Type type = new TypeName(fieldType.getGraphQLType());

    type = unique ? NonNullType.newNonNullType(type).build() : type;

    if (list) {
      type = ListType.newListType(type).build();
      type = unique ? NonNullType.newNonNullType(type).build() : type;
    }

    return type;
  }

  public List<Definition<?>> getDefinitions() {
    return new ArrayList<>(definitionMap.values());
  }

  public DataFetcher<Object> getDataFetcher(String key) {
    return this.dataFetcherMap.get(key);
  }

  public FieldTypeRegistry getFieldTypeRegistry() {
    return fieldTypeRegistry;
  }

  public Class<?> makeBeanClass(String namespace, Model model) {
    String classname = namespace.concat(".").concat(model.getCode());
    return AsmUtil.makeClass(classname);
  }

  private Class<?> makeEnumClass(String namespace, Model type) {
    String classname = namespace.concat(".").concat(type.getCode());
    return AsmUtil.makeEnum(
        classname, type.getFields().stream().map(ModelField::getCode).toArray(String[]::new));
  }

  private Definition<?> makeObjectTypeDefinition(Model type) {
    ObjectTypeDefinition.Builder subTypeBuilder =
        ObjectTypeDefinition.newObjectTypeDefinition().name(type.getCode());
    for (ModelField field : type.getFields()) {
      Type gType = getType(field.getType(), field.getUnique(), field.getList(), fieldTypeRegistry);
      subTypeBuilder.fieldDefinition(new FieldDefinition(field.getCode(), gType));
    }

    return subTypeBuilder.build();
  }

  public Map<String, Class<?>> getBeanClassMap() {
    return beanClassMap;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ModelResource {
    private Model model;
    private ModelRepository repository;
  }
}
