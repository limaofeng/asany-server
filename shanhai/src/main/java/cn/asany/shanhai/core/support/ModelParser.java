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
import cn.asany.shanhai.core.support.graphql.config.CustomTypeDefinitionFactory;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import cn.asany.shanhai.core.support.tools.DynamicClassGenerator;
import graphql.kickstart.autoconfigure.tools.SchemaDirective;
import graphql.kickstart.autoconfigure.tools.SchemaStringProvider;
import graphql.kickstart.tools.*;
import graphql.language.*;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLScalarType;
import graphql.schema.idl.SchemaDirectiveWiring;
import lombok.*;
import org.jfantasy.framework.util.FantasyClassLoader;
import org.jfantasy.framework.util.asm.AsmUtil;
import org.jfantasy.framework.util.asm.Property;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class ModelParser {

  @Autowired FieldTypeRegistry fieldTypeRegistry;
  @Autowired private ModelService modelService;
  @Autowired private ModelEndpointService modelEndpointService;
  @Autowired private ModelSessionFactory modelSessionFactory;
  @Autowired private ModelDelegateFactory delegateFactory;

  @Autowired private DynamicClassGenerator dynamicClassGenerator;
  private final Map<Long, ModelResource> modelResourceMap = new ConcurrentHashMap<>();
  private final Map<String, DataFetcher<Object>> dataFetcherMap = new ConcurrentHashMap<>();
  private final Map<String, Class<?>> beanClassMap = new ConcurrentHashMap<>();

  private final Map<String, Definition<?>> definitionMap = new ConcurrentHashMap<>();

  private final Map<String, List<FieldDefinition>> queryDefinitionMap = new ConcurrentHashMap<>();
  private final Map<String, GraphQLResolver<?>> queryResolverMap = new ConcurrentHashMap<>();

  private final Set<Model> allModels = new HashSet<>();

  private final List<GraphQLResolver<?>> resolvers;
  private final SchemaStringProvider schemaStringProvider;
  private final SchemaParserOptions.Builder optionsBuilder;
  private final SchemaParserDictionary dictionary;
  private final GraphQLScalarType[] scalars;
  private final List<SchemaDirective> directives;
  private final List<SchemaDirectiveWiring> directiveWirings;
  private SchemaParser schemaParser;

  public ModelParser(
      List<GraphQLResolver<?>> resolvers,
      SchemaStringProvider schemaStringProvider,
      SchemaParserOptions.Builder optionsBuilder,
      SchemaParserDictionary dictionary,
      GraphQLScalarType[] scalars,
      List<SchemaDirective> directives,
      List<SchemaDirectiveWiring> directiveWirings) {
    optionsBuilder.typeDefinitionFactory(new CustomTypeDefinitionFactory(this));

    this.resolvers = resolvers;
    this.schemaStringProvider = schemaStringProvider;
    this.optionsBuilder = optionsBuilder;
    this.dictionary = dictionary;
    this.scalars = scalars;
    this.directives = directives;
    this.directiveWirings = directiveWirings;
  }

  public List<Model> getModels() {
    return modelResourceMap.values().stream()
        .map(ModelResource::getModel)
        .collect(Collectors.toList());
  }

  public void init() throws IOException {
    List<Model> models = this.modelService.findEntityModels();

    for (Model m : models) {
      allModels.add(this.buildModel(m.getId()));
    }

    ObjectTypeExtensionDefinition.Builder queryBuilder =
        ObjectTypeExtensionDefinition.newObjectTypeExtensionDefinition().name("Query");

    for (Model m : models) {
      queryDefinitionMap.get(m.getCode()).forEach(queryBuilder::fieldDefinition);
    }

    // 添加接口定义
    definitionMap.put("Query", queryBuilder.build());

    this.schemaParser = this.buildSchemaParser();

    modelSessionFactory.update();
  }

  public void createModelField(Long modelId, ModelField field) {
    ObjectUtil.remove(allModels, "id", modelId);

    allModels.add(this.buildModel(modelId));

    modelSessionFactory.update();
  }

  public void deleteModelField(Long modelId, ModelField field) {
    ObjectUtil.remove(allModels, "id", modelId);

    allModels.add(this.buildModel(modelId));

    modelSessionFactory.update();
  }

  private Model buildModel(Long id) {
    Model model = this.modelService.getDetails(id);

    Class<?> entityClass =
        dynamicClassGenerator.makeEntityClass(model.getModule().getCode(), model);

    ModelRepository modelRepository = modelSessionFactory.buildModelRepository(model);

    this.modelResourceMap.put(
        model.getId(), ModelResource.builder().model(model).repository(modelRepository).build());

    // 生成主对象
    definitionMap.put(model.getCode(), this.makeObjectTypeDefinition(model));
    beanClassMap.put(model.getCode(), entityClass);

    // 检测依赖对象
    for (ModelRelation relation : model.getRelations()) {
      Model inverse = this.modelService.getDetails(relation.getInverse().getId());
      relation.setInverse(inverse);
      if (relation.getType() != ModelRelationType.SUBJECTION) {
        continue;
      }
      Model type = relation.getInverse();
      type.setModule(model.getModule());

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
        definitionMap.put(type.getCode(), this.makeInputTypeDefinition(type));
        beanClassMap.put(type.getCode(), this.makeBeanClass(model.getModule().getCode(), type));
      }
    }

    List<ModelEndpoint> endpoints = this.modelEndpointService.listEndpoints(model.getId());
    model.setEndpoints(new HashSet<>(endpoints));

    this.queryResolverMap.put(model.getCode(), this.makeQueryResolver(modelRepository, model));

    List<FieldDefinition> queryDefinitions = new ArrayList<>();
    for (ModelEndpoint endpoint : endpoints) {
      // 生成接口的定义
      if (ModelEndpointType.LIST == endpoint.getType()
      /*|| endpoint.getType() == ModelEndpointType.GET
      || endpoint.getType() == ModelEndpointType.CONNECTION*/ ) {
        FieldDefinition fieldDefinition = buildEndpoint(endpoint);
        queryDefinitions.add(fieldDefinition);
        queryDefinitionMap.put(model.getCode(), queryDefinitions);
      } else {

      }

      // 生成接口的 DataFetcher
      dataFetcherMap.put(
          model.getCode() + "." + endpoint.getCode(),
          new ModelDataFetcher(
              delegateFactory.build(model, endpoint, modelRepository, endpoint.getDelegate())));
    }
    return model;
  }

  private FieldDefinition buildEndpoint(ModelEndpoint endpoint) {
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
    return fieldBuilder.build();
  }

  @SneakyThrows
  private GraphQLResolver<?> makeQueryResolver(ModelRepository repository, Model model) {
    String namespace = model.getModule().getCode();

    ModelEndpoint endpoint = ObjectUtil.find(model.getEndpoints(), "type", ModelEndpointType.LIST);

    Class<GraphQLResolver<?>> resolverClass =
        dynamicClassGenerator.makeQueryResolver(namespace, model.getCode(), endpoint.getCode());

    return resolverClass.getConstructor(ModelRepository.class).newInstance(repository);
  }

  private GraphQLResolver<?> makeMutationResolver(Model model) {
    return null;
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

  public Type<?> getType(
      String typeStr, boolean unique, boolean list, FieldTypeRegistry fieldTypeRegistry) {
    FieldType<?, ?> fieldType = fieldTypeRegistry.getType(typeStr);

    Type<?> type = new TypeName(fieldType.getGraphQLType());

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

  public Class<?> makeBeanClass(String namespace, Model model) {
    String classname = namespace.concat(".").concat(model.getCode());
    return AsmUtil.makeClass(
        classname,
        model.getFields().stream()
            .map(
                item -> {
                  Property.PropertyBuilder builder = Property.builder().name(item.getCode());
                  if (item.getRealType() == model) {
                    if (item.getList()) {
                      builder
                          .descriptor("Ljava/util/List;")
                          .signature("Ljava/util/List<L" + classname.replace(".", "/") + ";>;");
                    } else {
                      builder.descriptor("L" + classname.replace(".", "/") + ";");
                    }
                  } else {
                    ModelFieldMetadata metadata =
                        item.getMetadata() == null
                            ? ModelFieldMetadata.builder().build()
                            : item.getMetadata();
                    metadata.setField(item);
                    String javaType =
                        fieldTypeRegistry.getType(item.getType()).getJavaType(metadata);
                    try {
                      Class<?> javaTypeClass =
                          FantasyClassLoader.getClassLoader().loadClass(javaType);
                      if (item.getList()) {
                        builder.type(List.class).genericTypes(new Class[] {javaTypeClass});
                      } else {
                        builder.type(javaTypeClass);
                      }
                    } catch (ClassNotFoundException e) {
                      throw new RuntimeException(e);
                    }
                  }
                  return builder.build();
                })
            .toArray(Property[]::new));
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
      subTypeBuilder.fieldDefinition(
          new FieldDefinition(
              field.getCode(),
              getType(field.getType(), field.getUnique(), field.getList(), fieldTypeRegistry)));
    }

    return subTypeBuilder.build();
  }

  private Definition<?> makeInputTypeDefinition(Model model) {
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
                .description(
                    new Description(
                        field.getName() + "<br/>" + field.getDescription(),
                        new SourceLocation(0, 0),
                        true))
                .type(
                    getType(
                        field.getType(), field.getRequired(), field.getList(), fieldTypeRegistry))
                .build());
      }
    }
    return inputBuilder.build();
  }

  public Map<String, Class<?>> getBeanClassMap() {
    return beanClassMap;
  }

  public List<GraphQLResolver<?>> getResolvers() {
    return new ArrayList<>(this.queryResolverMap.values());
  }

  public SchemaParser getSchemaParser() {
    return this.schemaParser;
  }

  public SchemaParser buildSchemaParser() throws IOException {
    SchemaParserBuilder builder = new SchemaParserBuilder();

    SchemaParserDictionary _dictionary = new SchemaParserDictionary();

    for (Map.Entry<String, Class<?>> entry : beanClassMap.entrySet()) {
      _dictionary.add(entry.getKey(), entry.getValue());
    }

    if (nonNull(dictionary)) {
      _dictionary.add(dictionary.getDictionary());
    }

    builder.dictionary(_dictionary.getDictionary());

    List<String> schemaStrings = schemaStringProvider.schemaStrings();
    schemaStrings.forEach(builder::schemaString);
    if (scalars != null) {
      builder.scalars(scalars);
    }
    builder.options(optionsBuilder.build());
    if (directives != null) {
      directives.forEach(it -> builder.directive(it.getName(), it.getDirective()));
    }
    if (directiveWirings != null) {
      directiveWirings.forEach(builder::directiveWiring);
    }

    List<GraphQLResolver<?>> allResolvers = new ArrayList<>();
    allResolvers.addAll(resolvers);
    allResolvers.addAll(this.getResolvers());

    return builder.resolvers(allResolvers).build();
  }

  public SchemaParser rebuildSchemaParser() throws IOException {
    return this.schemaParser = this.buildSchemaParser();
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
