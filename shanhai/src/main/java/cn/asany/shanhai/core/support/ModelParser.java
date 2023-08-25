package cn.asany.shanhai.core.support;

import static java.util.Objects.nonNull;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelEndpoint;
import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.core.domain.ModelRelation;
import cn.asany.shanhai.core.domain.enums.ModelConnectType;
import cn.asany.shanhai.core.domain.enums.ModelEndpointType;
import cn.asany.shanhai.core.domain.enums.ModelRelationType;
import cn.asany.shanhai.core.listener.StopWatchHolder;
import cn.asany.shanhai.core.service.ModelEndpointService;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.dao.ModelSessionFactory;
import cn.asany.shanhai.core.support.graphql.config.CustomTypeDefinitionFactory;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import cn.asany.shanhai.core.support.tools.DynamicClassGenerator;
import cn.asany.shanhai.core.utils.GraphQLTypeUtils;
import graphql.kickstart.autoconfigure.tools.SchemaDirective;
import graphql.kickstart.autoconfigure.tools.SchemaStringProvider;
import graphql.kickstart.tools.*;
import graphql.language.Definition;
import graphql.language.FieldDefinition;
import graphql.language.ObjectTypeExtensionDefinition;
import graphql.schema.GraphQLScalarType;
import graphql.schema.idl.SchemaDirectiveWiring;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.mybatis.keygen.util.DataBaseKeyGenerator;
import org.jfantasy.framework.dao.util.JdbcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

/**
 * Model Parser
 *
 * @author limaofeng
 */
@Slf4j
public class ModelParser {

  @Autowired FieldTypeRegistry fieldTypeRegistry;
  @Autowired private ModelService modelService;
  @Autowired private ModelEndpointService modelEndpointService;
  @Autowired private ModelSessionFactory modelSessionFactory;
  @Autowired private DynamicClassGenerator dynamicClassGenerator;
  private final Map<Long, ModelResource> modelResourceMap = new ConcurrentHashMap<>();

  private final Map<Long, ModelMediator> allModels = new ConcurrentHashMap<>();

  private Definition<?> queryDefinition;
  private Definition<?> mutationDefinition;

  private final List<GraphQLResolver<?>> resolvers;
  private final SchemaStringProvider schemaStringProvider;
  private final SchemaParserOptions.Builder optionsBuilder;
  private final SchemaParserDictionary dictionary;
  private final GraphQLScalarType[] scalars;
  private final List<SchemaDirective> directives;
  private final List<SchemaDirectiveWiring> directiveWirings;
  @Getter private SchemaParser schemaParser;

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
    this.refreshMutationDefinition();

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

  private void refreshMutationDefinition() {
    ObjectTypeExtensionDefinition.Builder queryBuilder =
        ObjectTypeExtensionDefinition.newObjectTypeExtensionDefinition().name("Mutation");

    for (ModelMediator m : allModels.values()) {
      m.getMutationDefinitions().values().forEach(queryBuilder::fieldDefinition);
    }

    mutationDefinition = queryBuilder.build();
  }

  public void updateModel(Long id) {
    ModelMediator mediator = new ModelMediator(id);

    mediator.reinstall();

    this.refreshQueryDefinition();
    this.refreshMutationDefinition();

    modelSessionFactory.update();
  }

  public void createModel(Model model) {
    ModelMediator mediator = new ModelMediator(model.getId());

    allModels.put(model.getId(), mediator);

    mediator.install();

    this.refreshQueryDefinition();
    this.refreshMutationDefinition();

    modelSessionFactory.update();
  }

  public void deleteModel(Model model) {
    ModelMediator mediator = allModels.get(model.getId());
    mediator.uninstall();

    allModels.remove(model.getId());

    this.refreshQueryDefinition();
    this.refreshMutationDefinition();

    modelSessionFactory.update();
  }

  public void createModelField(Long modelId, @SuppressWarnings("unused") ModelField field) {
    ModelMediator mediator = allModels.get(modelId);

    mediator.reinstall();

    this.refreshQueryDefinition();
    this.refreshMutationDefinition();

    modelSessionFactory.update();
  }

  public void updateModelField(Long modelId, @SuppressWarnings("unused") ModelField field) {
    StopWatch sw = StopWatchHolder.get();

    ModelMediator mediator = allModels.get(modelId);

    mediator.reinstall();

    sw.start("刷新接口");
    this.refreshQueryDefinition();
    this.refreshMutationDefinition();
    sw.stop();

    sw.start("更新 Model Session Factory");
    modelSessionFactory.update();
    sw.stop();
  }

  public void deleteModelField(Long modelId, ModelField field) {
    ModelMediator mediator = allModels.get(modelId);

    mediator.reinstall();

    String tableName = mediator.model.getMetadata().getDatabaseTableName();
    String columnName = field.getMetadata().getDatabaseColumnName();

    JdbcUtils.dropColumn(tableName, columnName);

    this.refreshQueryDefinition();
    this.refreshMutationDefinition();

    modelSessionFactory.update();
  }

  public List<Definition<?>> getDefinitions() {
    List<Definition<?>> allDefinitions =
        allModels.values().stream()
            .reduce(
                new ArrayList<>(),
                (acc, item) -> {
                  acc.addAll(item.getTypeDefinitions().values());
                  return acc;
                },
                (acc, bcc) -> {
                  acc.addAll(bcc);
                  return acc;
                });
    allDefinitions.add(queryDefinition);
    allDefinitions.add(mutationDefinition);
    return allDefinitions;
  }

  public List<GraphQLResolver<?>> getResolvers() {
    return this.allModels.values().stream()
        .reduce(
            new ArrayList<>(),
            (list, item) -> {
              list.addAll(item.getResolvers());
              return list;
            },
            (ay, by) -> {
              ay.addAll(by);
              return ay;
            });
  }

  public SchemaParser buildSchemaParser() throws IOException {
    SchemaParserBuilder builder = new SchemaParserBuilder();

    SchemaParserDictionary schemaParserDictionary = new SchemaParserDictionary();

    for (ModelMediator mediator : allModels.values()) {
      schemaParserDictionary.add(mediator.dictionary);
    }

    if (nonNull(dictionary)) {
      schemaParserDictionary.add(dictionary.getDictionary());
    }

    builder.dictionary(schemaParserDictionary.getDictionary());

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

  @Getter
  public class ModelMediator {
    private final Long id;
    private String code;
    private Model model;
    private final Map<String, FieldDefinition> queryDefinitions = new ConcurrentHashMap<>();

    @Getter
    private final Map<String, FieldDefinition> mutationDefinitions = new ConcurrentHashMap<>();

    @Getter private final Map<String, Definition<?>> typeDefinitions = new ConcurrentHashMap<>();
    @Getter private GraphQLResolver<?> queryResolver;
    @Getter private GraphQLResolver<?> mutationResolver;
    private final Map<String, Class<?>> dictionary = new ConcurrentHashMap<>();

    public ModelMediator(Long id) {
      this.id = id;
    }

    public void install() {
      this.model = ModelParser.this.modelService.getDetails(id);
      this.code = model.getCode();

      StopWatch sw = StopWatchHolder.get();

      Class<?> entityClass =
          dynamicClassGenerator.makeEntityClass(model.getModule().getCode(), model);

      sw.start("生成 ModelRepository");
      ModelRepository modelRepository = modelSessionFactory.buildModelRepository(model);
      sw.stop();

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

        boolean isEdgeType =
            ModelConnectType.GRAPHQL_OBJECT_EDGE.name().equals(relation.getRelation());
        boolean isConnectionType =
            ModelConnectType.GRAPHQL_OBJECT_CONNECTION.name().equals(relation.getRelation());

        boolean isWhereType =
            ModelConnectType.GRAPHQL_INPUT_WHERE.name().equals(relation.getRelation());

        if (isWhereType) {
          typeDefinitions.put(type.getCode(), GraphQLTypeUtils.makeInputTypeDefinition(type));
          dynamicClassGenerator.makeWhereClass(model.getModule().getCode(), entityClass, type);
        } else if (isEdgeType || isConnectionType) {
          typeDefinitions.put(type.getCode(), GraphQLTypeUtils.makeObjectTypeDefinition(type));
          if (isEdgeType) {
            Class<?> edgeType =
                dynamicClassGenerator.makeEdgeClass(model.getModule().getCode(), entityClass, type);
            dictionary.put(type.getCode(), edgeType);
          } else {
            Class<?> connectionType =
                dynamicClassGenerator.makeConnectionClass(
                    model.getModule().getCode(), entityClass, type);
            dictionary.put(type.getCode(), connectionType);
          }
        } else if (ModelConnectType.GRAPHQL_ENUM_ORDER_BY.name().equals(relation.getRelation())) {
          // 生成排序枚举
          typeDefinitions.put(type.getCode(), GraphQLTypeUtils.makeEnumTypeDefinition(type));
          dictionary.put(
              type.getCode(),
              dynamicClassGenerator.makeEnumClass(model.getModule().getCode(), type));
        } else if (ModelConnectType.GRAPHQL_INPUT_CREATE.name().equals(relation.getRelation())
            || ModelConnectType.GRAPHQL_INPUT_UPDATE.name().equals(relation.getRelation())) {
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

      for (ModelEndpoint endpoint : endpoints) {
        if (ModelEndpointType.LIST == endpoint.getType()
            || endpoint.getType() == ModelEndpointType.GET
            || endpoint.getType() == ModelEndpointType.CONNECTION) {
          FieldDefinition fieldDefinition = GraphQLTypeUtils.makeQueryFieldDefinition(endpoint);
          queryDefinitions.put(endpoint.getCode(), fieldDefinition);
        } else {
          FieldDefinition fieldDefinition = GraphQLTypeUtils.makeMutationFieldDefinition(endpoint);
          mutationDefinitions.put(endpoint.getCode(), fieldDefinition);
        }
      }

      this.queryResolver = GraphQLTypeUtils.makeQueryResolver(modelRepository, model);
      this.mutationResolver = GraphQLTypeUtils.makeMutationResolver(modelRepository, model);
    }

    public void reinstall() {
      this.queryDefinitions.clear();
      this.dictionary.clear();
      this.queryResolver = null;
      this.install();
    }

    public void uninstall() {
      modelSessionFactory.unbuildModelRepository(model.getCode());
      JdbcUtils.dropTable(model.getMetadata().getDatabaseTableName());
      DataBaseKeyGenerator.getInstance()
          .reset(model.getMetadata().getDatabaseTableName().toLowerCase() + ":id");
    }

    public List<GraphQLResolver<?>> getResolvers() {
      List<GraphQLResolver<?>> resolvers = new ArrayList<>();
      resolvers.add(this.mutationResolver);
      resolvers.add(this.queryResolver);
      return resolvers;
    }
  }
}
