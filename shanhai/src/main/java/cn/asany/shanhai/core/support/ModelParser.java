package cn.asany.shanhai.core.support;

import static java.util.Objects.nonNull;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelEndpoint;
import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.core.domain.ModelRelation;
import cn.asany.shanhai.core.domain.enums.ModelConnectType;
import cn.asany.shanhai.core.domain.enums.ModelEndpointType;
import cn.asany.shanhai.core.domain.enums.ModelRelationType;
import cn.asany.shanhai.core.service.ModelEndpointService;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.dao.ModelSessionFactory;
import cn.asany.shanhai.core.support.graphql.ModelDelegateFactory;
import cn.asany.shanhai.core.support.graphql.config.CustomTypeDefinitionFactory;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import cn.asany.shanhai.core.support.tools.DynamicClassGenerator;
import cn.asany.shanhai.core.utils.GraphQLTypeUtils;
import cn.asany.shanhai.core.utils.JdbcUtil;
import graphql.kickstart.autoconfigure.tools.SchemaDirective;
import graphql.kickstart.autoconfigure.tools.SchemaStringProvider;
import graphql.kickstart.tools.*;
import graphql.language.*;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLScalarType;
import graphql.schema.idl.SchemaDirectiveWiring;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.*;
import org.jfantasy.framework.util.asm.AsmUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class ModelParser {

  @Autowired FieldTypeRegistry fieldTypeRegistry;
  @Autowired private ModelService modelService;
  @Autowired private ModelEndpointService modelEndpointService;
  @Autowired private ModelSessionFactory modelSessionFactory;
  @Autowired private ModelDelegateFactory delegateFactory;

  @Autowired private DynamicClassGenerator dynamicClassGenerator;
  private final Map<Long, ModelResource> modelResourceMap = new ConcurrentHashMap<>();

  private final Map<Long, ModelMediator> allModels = new ConcurrentHashMap<>();

  private Definition<?> queryDefinition;

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
      ModelMediator mediator = new ModelMediator(m.getId());
      allModels.put(m.getId(), mediator);
      mediator.install();
    }

    this.refreshQueryDefinition();

    this.schemaParser = this.buildSchemaParser();

    modelSessionFactory.update();
  }

  private void refreshQueryDefinition() {
    ObjectTypeExtensionDefinition.Builder queryBuilder =
        ObjectTypeExtensionDefinition.newObjectTypeExtensionDefinition().name("Query");

    for (ModelMediator m : allModels.values()) {
      m.getQueryDefinitions().values().forEach(queryBuilder::fieldDefinition);
    }

    queryDefinition = queryBuilder.build();
  }

  public void updateModel(Long id) {
    //    ObjectUtil.remove(allModels, "id", id);
    //
    //    allModels.add(this.buildModel(id));
    //
    //    modelSessionFactory.update();
  }

  public void createModel(Model model) {
    ModelMediator mediator = new ModelMediator(model.getId());

    allModels.put(model.getId(), mediator);

    mediator.install();

    this.refreshQueryDefinition();

    modelSessionFactory.update();
  }

  public void deleteModel(Model model) {
    ModelMediator mediator = allModels.remove(model.getId());
    mediator.uninstall();

    this.refreshQueryDefinition();

    modelSessionFactory.update();
  }

  public void createModelField(Long modelId, ModelField field) {
    ModelMediator mediator = allModels.get(modelId);

    mediator.reinstall();

    modelSessionFactory.update();
  }

  public void updateModelField(Long modelId, ModelField field) {
    ModelMediator mediator = allModels.get(modelId);

    mediator.reinstall();

    modelSessionFactory.update();
  }

  public void deleteModelField(Long modelId, ModelField field) {
    ModelMediator mediator = allModels.get(modelId);

    mediator.reinstall();

    String tableName = mediator.model.getMetadata().getDatabaseTableName();
    String columnName = field.getMetadata().getDatabaseColumnName();

    JdbcUtil.dropColumn(tableName, columnName);

    modelSessionFactory.update();
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

  public List<Definition<?>> getDefinitions() {
    List<Definition<?>> allDefinitions =
        allModels.values().stream()
            .reduce(
                new ArrayList<>(),
                (acc, _item) -> {
                  acc.addAll(_item.getTypeDefinitions().values());
                  return acc;
                },
                (acc, bcc) -> {
                  acc.addAll(bcc);
                  return acc;
                });
    allDefinitions.add(queryDefinition);
    return allDefinitions;
  }

  public DataFetcher<Object> getDataFetcher(String key) {
    return null;
  }

  private Class<?> makeEnumClass(String namespace, Model type) {
    String classname = namespace.concat(".").concat(type.getCode());
    return AsmUtil.makeEnum(
        classname, type.getFields().stream().map(ModelField::getCode).toArray(String[]::new));
  }

  public List<GraphQLResolver<?>> getResolvers() {
    return this.allModels.values().stream()
        .map(item -> item.queryResolver)
        .collect(Collectors.toList());
  }

  public SchemaParser getSchemaParser() {
    return this.schemaParser;
  }

  public SchemaParser buildSchemaParser() throws IOException {
    SchemaParserBuilder builder = new SchemaParserBuilder();

    SchemaParserDictionary _dictionary = new SchemaParserDictionary();

    for (ModelMediator mediator : allModels.values()) {
      _dictionary.add(mediator.dictionary);
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

  public class ModelMediator {
    private final Long id;
    private String code;
    private Model model;

    @Getter private final Map<String, FieldDefinition> queryDefinitions = new ConcurrentHashMap<>();

    @Getter private final Map<String, Definition<?>> typeDefinitions = new ConcurrentHashMap<>();
    @Getter private GraphQLResolver<?> queryResolver;

    private final Map<String, Class<?>> dictionary = new ConcurrentHashMap<>();

    public ModelMediator(Long id) {
      this.id = id;
    }

    public void install() {
      this.model = ModelParser.this.modelService.getDetails(id);
      this.code = model.getCode();

      Class<?> entityClass =
          dynamicClassGenerator.makeEntityClass(model.getModule().getCode(), model);

      ModelRepository modelRepository = modelSessionFactory.buildModelRepository(model);

      ModelParser.this.modelResourceMap.put(
          model.getId(), ModelResource.builder().model(model).repository(modelRepository).build());

      // 生成主对象
      typeDefinitions.put(model.getCode(), GraphQLTypeUtils.makeObjectTypeDefinition(model));
      dictionary.put(model.getCode(), entityClass);

      // 检测依赖对象
      for (ModelRelation relation : model.getRelations()) {
        Model inverse = ModelParser.this.modelService.getDetails(relation.getInverse().getId());
        relation.setInverse(inverse);
        if (relation.getType() != ModelRelationType.SUBJECTION) {
          continue;
        }
        Model type = relation.getInverse();
        type.setModule(model.getModule());

        if (ModelConnectType.GRAPHQL_OBJECT_EDGE.name().equals(relation.getRelation())
            || ModelConnectType.GRAPHQL_OBJECT_CONNECTION.name().equals(relation.getRelation())) {
          // 生成的分页相关对象
          typeDefinitions.put(type.getCode(), GraphQLTypeUtils.makeObjectTypeDefinition(type));
          dictionary.put(
              type.getCode(),
              dynamicClassGenerator.makeBeanClass(model.getModule().getCode(), type));

        } else if (ModelConnectType.GRAPHQL_ENUM_ORDER_BY.name().equals(relation.getRelation())) {
          // 生成排序枚举
          typeDefinitions.put(type.getCode(), ModelParser.this.makeEnumTypeDefinition(type));
          dictionary.put(
              type.getCode(), ModelParser.this.makeEnumClass(model.getModule().getCode(), type));
        } else if (ModelConnectType.GRAPHQL_INPUT_CREATE.name().equals(relation.getRelation())
            || ModelConnectType.GRAPHQL_INPUT_UPDATE.name().equals(relation.getRelation())
            || ModelConnectType.GRAPHQL_INPUT_WHERE.name().equals(relation.getRelation())) {
          // 生成输入对象
          typeDefinitions.put(type.getCode(), GraphQLTypeUtils.makeInputTypeDefinition(type));
          dictionary.put(
              type.getCode(),
              dynamicClassGenerator.makeBeanClass(model.getModule().getCode(), type));
        }
      }

      List<ModelEndpoint> endpoints =
          ModelParser.this.modelEndpointService.listEndpoints(model.getId());
      model.setEndpoints(new HashSet<>(endpoints));

      this.queryResolver = GraphQLTypeUtils.makeQueryResolver(modelRepository, model);

      for (ModelEndpoint endpoint : endpoints) {
        // 生成接口的定义
        if (ModelEndpointType.LIST == endpoint.getType()
        /*|| endpoint.getType() == ModelEndpointType.GET
        || endpoint.getType() == ModelEndpointType.CONNECTION*/ ) {
          FieldDefinition fieldDefinition = GraphQLTypeUtils.makeQueryFieldDefinition(endpoint);
          queryDefinitions.put(endpoint.getCode(), fieldDefinition);
        } else {

        }
        // 生成接口的 DataFetcher
        //      dataFetcherMap.put(
        //          model.getCode() + "." + endpoint.getCode(),
        //          new ModelDataFetcher(
        //              delegateFactory.build(model, endpoint, modelRepository,
        // endpoint.getDelegate())));
      }
    }

    public void reinstall() {
      this.queryDefinitions.clear();
      this.dictionary.clear();
      this.queryResolver = null;
      this.install();
    }

    public void uninstall() {
      modelSessionFactory.unbuildModelRepository(model.getCode());
      JdbcUtil.dropTable(model.getMetadata().getDatabaseTableName());
    }

    public Long getId() {
      return id;
    }

    public String getCode() {
      return code;
    }

    public Model getModel() {
      return model;
    }
  }
}
