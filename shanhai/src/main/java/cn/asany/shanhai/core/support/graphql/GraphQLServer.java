package cn.asany.shanhai.core.support.graphql;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelEndpoint;
import cn.asany.shanhai.core.bean.enums.ModelEndpointType;
import cn.asany.shanhai.core.bean.enums.ModelType;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.utils.ModelUtils;
import cn.asany.shanhai.core.utils.TemplateDataOfEndpoint;
import cn.asany.shanhai.core.utils.TemplateDataOfModel;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.HandlebarsTemplateUtils;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * GraphQL Server
 *
 * @author limaofeng
 */
@Slf4j
public class GraphQLServer implements InitializingBean {

  @Autowired private ModelDelegateFactory delegateFactory;
  @Autowired private GraphQLScalarType dateScalar;
  @Autowired private ModelUtils modelUtils;

  private Template template;

  private GraphQL graphQL;
  private String scheme;
  private Map<Long, Model> modelMap = new ConcurrentHashMap<>();
  private Map<Long, Model> typeMap = new ConcurrentHashMap<>();
  private Map<Long, Model> inputTypeMap = new ConcurrentHashMap<>();
  private Map<Long, Model> enumMap = new ConcurrentHashMap<>();
  private Map<Long, Model> scalarMap = new ConcurrentHashMap<>();
  private Map<Long, List<Long>> modeTypeMap = new ConcurrentHashMap<>();
  private Map<Long, List<Long>> modelEndpointMap = new ConcurrentHashMap<>();
  private Map<Long, ModelEndpoint> queries = new ConcurrentHashMap();
  private Map<ModelEndpoint, DataFetcher> dataFetcherMap = new ConcurrentHashMap();
  private Map<Long, ModelEndpoint> mutations = new ConcurrentHashMap();

  @Override
  public void afterPropertiesSet() throws Exception {
    HandlebarsTemplateUtils.registerHelper(
        "notNull",
        (String context, Options options) -> {
          if (StringUtil.isNotBlank(context)) {
            return options.fn();
          }
          return options.inverse();
        });
    template = HandlebarsTemplateUtils.template("/scheme");
  }

  @SneakyThrows
  @Transactional
  public String buildScheme() {
    modelUtils.clear();

    List<Model> models = new ArrayList<>(modelMap.values());
    List<ModelEndpoint> queries = new ArrayList<>(this.queries.values());
    List<ModelEndpoint> mutations = new ArrayList<>(this.mutations.values());
    List<Model> enumerations = new ArrayList<>(this.enumMap.values());
    List<Model> types = new ArrayList<>(models);
    types.addAll(this.typeMap.values());
    List<Model> inputTypes = new ArrayList<>(this.inputTypeMap.values());
    List<Model> scalars = new ArrayList<>(this.scalarMap.values());

    modelUtils.newCache(types, inputTypes, scalars);

    return this.scheme =
        template.apply(
            new TemplateRootData(queries, mutations, types, inputTypes, scalars, enumerations));
  }

  public void addModel(Model model, ModelRepository repository) {
    this.buildResolver(model, repository);
  }

  private void buildResolver(Model model, ModelRepository repository) {
    List<Long> endpointIds = new ArrayList<>();
    modelMap.put(model.getId(), model);
    modelEndpointMap.put(model.getId(), endpointIds);
    for (ModelEndpoint endpoint : model.getEndpoints()) {
      endpointIds.add(endpoint.getId());
      if (endpoint.getType() == ModelEndpointType.QUERY) {
        queries.put(endpoint.getId(), endpoint);
      } else {
        mutations.put(endpoint.getId(), endpoint);
      }
      if (endpoint.getDelegate() == null) {
        dataFetcherMap.put(endpoint, new StaticDataFetcher("world"));
        continue;
      }
      dataFetcherMap.put(
          endpoint,
          new ModelDataFetcher(
              delegateFactory.build(model, endpoint, repository, endpoint.getDelegate())));
    }
  }

  public GraphQL buildServer() {
    SchemaParser schemaParser = new SchemaParser();
    TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(this.buildScheme());

    RuntimeWiring.Builder runtimeWiringBuilder = newRuntimeWiring();
    runtimeWiringBuilder.type(
        "Query",
        builder -> {
          for (ModelEndpoint endpoint : queries.values()) {
            builder.dataFetcher(endpoint.getCode(), dataFetcherMap.get(endpoint));
          }
          return builder;
        });
    runtimeWiringBuilder.type(
        "Mutation",
        builder -> {
          for (ModelEndpoint endpoint : mutations.values()) {
            builder.dataFetcher(endpoint.getCode(), dataFetcherMap.get(endpoint));
          }
          return builder;
        });
    runtimeWiringBuilder.scalar(dateScalar);
    RuntimeWiring runtimeWiring = runtimeWiringBuilder.build();
    SchemaGenerator schemaGenerator = new SchemaGenerator();
    GraphQLSchema graphQLSchema =
        schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

    return this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
  }

  public Map<String, Object> execute(
      String query, String operationName, Map<String, Object> variables) {
    ExecutionInput.Builder builder =
        ExecutionInput.newExecutionInput()
            .query(query)
            .operationName(operationName)
            .variables(variables);
    ExecutionResult executionResult = this.graphQL.execute(builder);

    Map<String, Object> result = new HashMap<>();
    if (!executionResult.getErrors().isEmpty()) {
      result.put("errors", executionResult.getErrors());
    }
    result.put("data", executionResult.getData());
    return result;
  }

  public void setTypes(List<Model> types) {
    for (Model type : types) {
      if (type.getType() == ModelType.OBJECT) {
        typeMap.put(type.getId(), type);
      } else if (type.getType() == ModelType.INPUT_OBJECT) {
        inputTypeMap.put(type.getId(), type);
      } else if (type.getType() == ModelType.SCALAR) {
        scalarMap.put(type.getId(), type);
      } else if (type.getType() == ModelType.ENUM) {
        enumMap.put(type.getId(), type);
      }
    }
  }

  static class TemplateRootData {
    private final List<ModelEndpoint> queries;
    private final List<ModelEndpoint> mutations;
    private final List<Model> types;
    private final List<Model> inputTypes;
    private final List<Model> enumerations;
    private final List<Model> scalars;

    public TemplateRootData(
        List<ModelEndpoint> queries,
        List<ModelEndpoint> mutations,
        List<Model> types,
        List<Model> inputTypes,
        List<Model> scalars,
        List<Model> enumerations) {
      this.types = types;
      this.scalars = scalars;
      this.inputTypes = inputTypes;
      this.queries = queries;
      this.mutations = mutations;
      this.enumerations = enumerations;
    }

    public List getMutations() {
      return mutations.stream()
          .map(item -> new TemplateDataOfEndpoint(item))
          .collect(Collectors.toList());
    }

    public List getQueries() {
      return queries.stream()
          .map(item -> new TemplateDataOfEndpoint(item))
          .collect(Collectors.toList());
    }

    public List<TemplateDataOfModel> getTypes() {
      return types.stream().map(item -> new TemplateDataOfModel(item)).collect(Collectors.toList());
    }

    public List<TemplateDataOfModel> getInputTypes() {
      return inputTypes.stream()
          .map(item -> new TemplateDataOfModel(item))
          .collect(Collectors.toList());
    }

    public List<TemplateDataOfModel> getScalars() {
      return scalars.stream()
          .map(item -> new TemplateDataOfModel(item))
          .collect(Collectors.toList());
    }

    public List<TemplateDataOfModel> getEnumerations() {
      return enumerations.stream()
          .map(item -> new TemplateDataOfModel(item))
          .collect(Collectors.toList());
    }
  }
}
