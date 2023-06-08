package cn.asany.shanhai.core.support.model.features;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelEndpoint;
import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.core.domain.ModelRelation;
import cn.asany.shanhai.core.domain.enums.ModelConnectType;
import cn.asany.shanhai.core.domain.enums.ModelEndpointType;
import cn.asany.shanhai.core.domain.enums.ModelRelationType;
import cn.asany.shanhai.core.domain.enums.ModelType;
import cn.asany.shanhai.core.support.graphql.resolvers.base.*;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import cn.asany.shanhai.core.support.model.IModelFeature;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jfantasy.framework.dao.MatchType;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Data
@Component
public class MasterModelFeature implements IModelFeature, InitializingBean {
  public static final String ID = "master";
  private String id = ID;

  public static final String SUFFIX_INPUT_WHERE = "WhereInput";
  public static final String SUFFIX_INPUT_CREATE = "CreateInput";
  public static final String SUFFIX_INPUT_UPDATE = "UpdateInput";

  public static final String SUFFIX_ENUM_ORDER_BY = "OrderBy";

  public static final String SUFFIX_TYPE_EDGE = "Edge";

  public static final String SUFFIX_TYPE_CONNECTION = "Connection";

  public static final String ENDPOINT_PREFIX_CREATE = "create";

  public static final String ENDPOINT_PREFIX_UPDATE = "update";

  public static final String ENDPOINT_PREFIX_DELETE = "delete";

  public static final String ENDPOINT_PREFIX_DELETE_MANY = "deleteMany";

  private final FieldTypeRegistry fieldTypeRegistry;

  private List<RuleAndReplacement> plurals = new ArrayList<>();

  public MasterModelFeature(FieldTypeRegistry fieldTypeRegistry) {
    this.fieldTypeRegistry = fieldTypeRegistry;
  }

  @Override
  public List<ModelEndpoint> getEndpoints(Model model) {
    List<ModelEndpoint> endpoints = new ArrayList<>();
    endpoints.add(buildCreateEndpoint(model));
    endpoints.add(buildUpdateEndpoint(model));
    endpoints.add(buildDeleteEndpoint(model));
    endpoints.add(buildDeleteManyEndpoint(model));
    endpoints.add(buildGetEndpoint(model));
    endpoints.add(buildFindAllEndpoint(model));
    endpoints.add(buildFindPaginationEndpoint(model));
    return endpoints;
  }

  private Set<ModelField> cloneInputFields(Set<ModelField> fields) {
    return cloneModelFields(fields, item -> !item.getPrimaryKey() && !item.getSystem());
  }

  private Set<ModelField> cloneModelFields(
      Set<ModelField> fields, Predicate<ModelField> predicate) {
    return fields.stream()
        .filter(predicate)
        .map(
            item ->
                ModelField.builder()
                    .code(item.getCode())
                    .name(item.getName())
                    .list(item.getList())
                    .type(item.getRealType().getCode())
                    .build())
        .collect(Collectors.toSet());
  }

  private Model buildType(ModelType type, String code, String name, Set<ModelField> fields) {
    return Model.builder().type(type).code(code).name(name).fields(fields).build();
  }

  private static String getCreateInputTypeName(Model model) {
    return model.getCode() + SUFFIX_INPUT_CREATE;
  }

  private static String getUpdateInputTypeName(Model model) {
    return model.getCode() + SUFFIX_INPUT_UPDATE;
  }

  private static String getWhereInputTypeName(Model model) {
    return model.getCode() + SUFFIX_INPUT_WHERE;
  }

  private static String getOrderByTypeName(Model model) {
    return model.getCode() + SUFFIX_ENUM_ORDER_BY;
  }

  private static String getConnectionTypeName(Model model) {
    return model.getCode() + SUFFIX_TYPE_CONNECTION;
  }

  private Set<ModelField> buildWhereFields(Model model) {
    Set<ModelField> fields = new HashSet<>();
    fields.add(
        ModelField.builder()
            .code("AND")
            .type(getWhereInputTypeName(model))
            .list(true)
            .name("逻辑与.")
            .build());
    fields.add(
        ModelField.builder()
            .code("OR")
            .type(getWhereInputTypeName(model))
            .list(true)
            .name("逻辑或.")
            .build());
    fields.add(
        ModelField.builder()
            .code("NOT")
            .type(getWhereInputTypeName(model))
            .list(true)
            .name("对所有由 AND 组合的给定过滤器进行逻辑非.")
            .build());
    for (ModelField field : new ArrayList<>(model.getFields())) {
      if (field.getRealType().getType() == ModelType.SCALAR) {
        for (MatchType matchType : field.getMetadata().getFilters()) {
          String fieldName =
              matchType == MatchType.EQ
                  ? field.getCode()
                  : field.getCode() + "_" + matchType.getSlug();

          boolean isList = matchType == MatchType.IN || matchType == MatchType.NOT_IN;
          fields.add(
              ModelField.builder()
                  .code(fieldName)
                  .type(field.getRealType().getCode())
                  .list(isList)
                  .build());
        }
      }
    }
    return fields;
  }

  private Set<ModelField> buildEdgeFields(Model model) {
    Set<ModelField> fields = new HashSet<>();
    fields.add(
        ModelField.builder()
            .code("node")
            .type(model.getCode())
            .required(true)
            .name("数据节点.")
            .build());
    fields.add(
        ModelField.builder()
            .code("cursor")
            .type(FieldType.String)
            .required(true)
            .name("分页游标.")
            .build());
    return fields;
  }

  private static Set<ModelField> buildOrderByFields(Model model) {
    Set<ModelField> fields = new HashSet<>();
    for (ModelField field :
        model.getFields().stream()
            .filter(item -> !item.getPrimaryKey())
            .collect(Collectors.toList())) {

      if (!field.getMetadata().getSortable()) {
        continue;
      }

      fields.add(
          ModelField.builder()
              .code(field.getCode() + "_ASC")
              .name(field.getName() + " 升序")
              .type(field.getRealType().getCode())
              .build());
      fields.add(
          ModelField.builder()
              .code(field.getCode() + "_DESC")
              .name(field.getName() + " 降序")
              .type(field.getRealType().getCode())
              .build());
    }
    return fields;
  }

  private static Set<ModelField> buildConnectionFields(Model model) {
    Set<ModelField> fields = new HashSet<>();
    fields.add(
        ModelField.builder()
            .code("totalCount")
            .type(FieldType.Int)
            .name("总数据条数")
            .required(true)
            .build());
    fields.add(
        ModelField.builder()
            .code("pageSize")
            .type(FieldType.Int)
            .name("每页数据条数")
            .required(true)
            .build());
    fields.add(
        ModelField.builder()
            .code("totalPage")
            .type(FieldType.Int)
            .name("总页数")
            .required(true)
            .build());
    fields.add(
        ModelField.builder()
            .code("currentPage")
            .type(FieldType.Int)
            .name("当前页码")
            .required(true)
            .build());
    fields.add(
        ModelField.builder()
            .code("edges")
            .type(getEdgeTypeName(model))
            .list(true)
            .required(true)
            .name("A list of edges.")
            .build());
    return fields;
  }

  private void initFields(Model type, Set<ModelField> fields) {
    type.setFields(
        fields.stream()
            .peek(
                f -> {
                  Optional<ModelField> fieldOptional =
                      type.getFields().stream()
                          .filter(f1 -> f.getCode().equals(f1.getCode()))
                          .findFirst();
                  fieldOptional.ifPresent(modelField -> f.setId(modelField.getId()));
                })
            .collect(Collectors.toSet()));
  }

  @Override
  public List<ModelRelation> getTypes(Model model) {
    Set<ModelRelation> allRelations = model.getRelations();

    ModelRelation inputTypeOfCreate =
        allRelations.stream()
            .filter(item -> item.getRelation().equals(ModelConnectType.GRAPHQL_INPUT_CREATE.name()))
            .findFirst()
            .map(
                item -> {
                  Model type = item.getInverse();
                  type.setCode(getCreateInputTypeName(model));
                  type.setName(model.getName() + "录入对象");
                  initFields(type, cloneInputFields(model.getFields()));
                  return item;
                })
            .orElseGet(
                () -> {
                  Model type =
                      this.buildType(
                          ModelType.INPUT_OBJECT,
                          getCreateInputTypeName(model),
                          model.getName() + "录入对象",
                          cloneInputFields(model.getFields()));
                  return ModelRelation.builder()
                      .type(ModelRelationType.SUBJECTION)
                      .relation(ModelConnectType.GRAPHQL_INPUT_CREATE.name())
                      .inverse(type)
                      .build();
                });

    ModelRelation inputTypeOfUpdate =
        allRelations.stream()
            .filter(item -> item.getRelation().equals(ModelConnectType.GRAPHQL_INPUT_UPDATE.name()))
            .findFirst()
            .map(
                item -> {
                  Model type = item.getInverse();
                  type.setCode(getUpdateInputTypeName(model));
                  type.setName(model.getName() + "更新对象");
                  initFields(type, cloneInputFields(model.getFields()));
                  return item;
                })
            .orElseGet(
                () -> {
                  Model type =
                      this.buildType(
                          ModelType.INPUT_OBJECT,
                          getUpdateInputTypeName(model),
                          model.getName() + "更新对象",
                          cloneInputFields(model.getFields()));
                  return ModelRelation.builder()
                      .type(ModelRelationType.SUBJECTION)
                      .relation(ModelConnectType.GRAPHQL_INPUT_UPDATE.name())
                      .inverse(type)
                      .build();
                });

    ModelRelation inputTypeOfFilter =
        allRelations.stream()
            .filter(item -> item.getRelation().equals(ModelConnectType.GRAPHQL_INPUT_WHERE.name()))
            .findFirst()
            .map(
                item -> {
                  Model type = item.getInverse();
                  type.setCode(getWhereInputTypeName(model));
                  type.setName(model.getName() + "过滤器");
                  initFields(type, buildWhereFields(model));
                  return item;
                })
            .orElseGet(
                () -> {
                  Model type =
                      this.buildType(
                          ModelType.INPUT_OBJECT,
                          getWhereInputTypeName(model),
                          model.getName() + "过滤器",
                          buildWhereFields(model));
                  return ModelRelation.builder()
                      .type(ModelRelationType.SUBJECTION)
                      .relation(ModelConnectType.GRAPHQL_INPUT_WHERE.name())
                      .inverse(type)
                      .build();
                });
    ModelRelation inputTypeOfOrderBy =
        allRelations.stream()
            .filter(
                item -> item.getRelation().equals(ModelConnectType.GRAPHQL_ENUM_ORDER_BY.name()))
            .findFirst()
            .map(
                item -> {
                  Model type = item.getInverse();
                  type.setCode(getOrderByTypeName(model));
                  type.setName(model.getName() + "排序");
                  initFields(type, buildOrderByFields(model));
                  return item;
                })
            .orElseGet(
                () -> {
                  Model type =
                      this.buildType(
                          ModelType.ENUM,
                          getOrderByTypeName(model),
                          model.getName() + "排序",
                          buildOrderByFields(model));
                  return ModelRelation.builder()
                      .type(ModelRelationType.SUBJECTION)
                      .relation(ModelConnectType.GRAPHQL_ENUM_ORDER_BY.name())
                      .inverse(type)
                      .build();
                });
    ModelRelation typeOfEdge =
        allRelations.stream()
            .filter(item -> item.getRelation().equals(ModelConnectType.GRAPHQL_OBJECT_EDGE.name()))
            .findFirst()
            .map(
                item -> {
                  Model type = item.getInverse();
                  type.setCode(getEdgeTypeName(model));
                  type.setName(model.getName() + "数据集.");
                  initFields(type, buildEdgeFields(model));
                  return item;
                })
            .orElseGet(
                () -> {
                  Model type =
                      this.buildType(
                          ModelType.OBJECT,
                          getEdgeTypeName(model),
                          model.getName() + " 数据集.",
                          buildEdgeFields(model));
                  return ModelRelation.builder()
                      .type(ModelRelationType.SUBJECTION)
                      .relation(ModelConnectType.GRAPHQL_OBJECT_EDGE.name())
                      .inverse(type)
                      .build();
                });
    ModelRelation typeOfConnection =
        allRelations.stream()
            .filter(
                item ->
                    item.getRelation().equals(ModelConnectType.GRAPHQL_OBJECT_CONNECTION.name()))
            .findFirst()
            .map(
                item -> {
                  Model type = item.getInverse();
                  type.setCode(getConnectionTypeName(model));
                  type.setName(model.getName() + "分页对象.");
                  initFields(type, buildConnectionFields(model));
                  return item;
                })
            .orElseGet(
                () -> {
                  Model type =
                      this.buildType(
                          ModelType.OBJECT,
                          getConnectionTypeName(model),
                          model.getName() + " 分页对象",
                          buildConnectionFields(model));
                  return ModelRelation.builder()
                      .type(ModelRelationType.SUBJECTION)
                      .relation(ModelConnectType.GRAPHQL_OBJECT_CONNECTION.name())
                      .inverse(type)
                      .build();
                });
    return new ArrayList<>(
        Arrays.asList(
            inputTypeOfCreate,
            inputTypeOfUpdate,
            inputTypeOfFilter,
            inputTypeOfOrderBy,
            typeOfEdge,
            typeOfConnection));
  }

  private static String getEdgeTypeName(Model model) {
    return model.getCode() + SUFFIX_TYPE_EDGE;
  }

  private ModelEndpoint buildCreateEndpoint(Model model) {
    String code = ENDPOINT_PREFIX_CREATE + StringUtil.upperCaseFirst(model.getCode());
    String name = "新增" + model.getName();
    return model.getEndpoints().stream()
        .filter(item -> item.getType() == ModelEndpointType.CREATE)
        .findFirst()
        .map(
            item -> {
              item.setCode(code);
              item.setName(name);
              item.getArguments().stream()
                  .filter(a -> "input".equals(a.getName()))
                  .findAny()
                  .map(
                      (a) -> {
                        a.setType(getCreateInputTypeName(model));
                        return a;
                      });
              return item;
            })
        .orElseGet(
            () -> {
              ModelEndpoint endpoint =
                  ModelEndpoint.builder()
                      .type(ModelEndpointType.CREATE)
                      .code(code)
                      .name(name)
                      .argument("input", getCreateInputTypeName(model), true)
                      .returnType(model)
                      .model(model)
                      .delegate(BaseMutationCreateDataFetcher.class)
                      .build();
              endpoint.getReturnType().setEndpoint(endpoint);
              return endpoint;
            });
  }

  private ModelEndpoint buildUpdateEndpoint(Model model) {
    String code = ENDPOINT_PREFIX_UPDATE + StringUtil.upperCaseFirst(model.getCode());
    String name = "修改" + model.getName();
    return model.getEndpoints().stream()
        .filter(item -> item.getType() == ModelEndpointType.UPDATE)
        .findFirst()
        .map(
            item -> {
              item.setCode(code);
              item.setName(name);
              item.getArguments().stream()
                  .filter(a -> "input".equals(a.getName()))
                  .findAny()
                  .map(
                      (a) -> {
                        a.setType(getUpdateInputTypeName(model));
                        return a;
                      });
              return item;
            })
        .orElseGet(
            () -> {
              ModelEndpoint endpoint =
                  ModelEndpoint.builder()
                      .type(ModelEndpointType.UPDATE)
                      .code(code)
                      .name(name)
                      .argument("id", FieldType.ID, true)
                      .argument("input", getUpdateInputTypeName(model), true)
                      .argument("merge", FieldType.Boolean, "启用合并模式", true)
                      .returnType(model)
                      .model(model)
                      .delegate(BaseMutationUpdateDataFetcher.class)
                      .build();
              endpoint.getReturnType().setEndpoint(endpoint);
              return endpoint;
            });
  }

  private ModelEndpoint buildDeleteEndpoint(Model model) {
    String code = ENDPOINT_PREFIX_DELETE + StringUtil.upperCaseFirst(model.getCode());
    String name = "删除" + model.getName();
    return model.getEndpoints().stream()
        .filter(item -> item.getType() == ModelEndpointType.DELETE)
        .findFirst()
        .map(
            item -> {
              item.setCode(code);
              item.setName(name);
              return item;
            })
        .orElseGet(
            () -> {
              ModelEndpoint endpoint =
                  ModelEndpoint.builder()
                      .type(ModelEndpointType.DELETE)
                      .code(code)
                      .name(name)
                      .argument("id", FieldType.ID, true)
                      .returnType(model)
                      .model(model)
                      .delegate(BaseMutationDeleteDataFetcher.class)
                      .build();
              endpoint.getReturnType().setEndpoint(endpoint);
              return endpoint;
            });
  }

  private ModelEndpoint buildDeleteManyEndpoint(Model model) {
    String code = ENDPOINT_PREFIX_DELETE_MANY + StringUtil.upperCaseFirst(model.getCode() + "s");
    String name = "批量删除" + model.getName();
    return model.getEndpoints().stream()
        .filter(item -> item.getType() == ModelEndpointType.DELETE_MANY)
        .findFirst()
        .map(
            item -> {
              item.setCode(code);
              item.setName(name);
              return item;
            })
        .orElseGet(
            () -> {
              ModelEndpoint endpoint =
                  ModelEndpoint.builder()
                      .type(ModelEndpointType.DELETE_MANY)
                      .code(code)
                      .name(name)
                      .argument("where", getWhereInputTypeName(model), true)
                      .returnType(true, model)
                      .model(model)
                      .delegate(BaseMutationDeleteDataFetcher.class)
                      .build();
              endpoint.getReturnType().setEndpoint(endpoint);
              return endpoint;
            });
  }

  private ModelEndpoint buildGetEndpoint(Model model) {
    String code = StringUtil.lowerCaseFirst(model.getCode());
    String name = "获取" + model.getName();
    return model.getEndpoints().stream()
        .filter(item -> item.getType() == ModelEndpointType.GET)
        .findFirst()
        .map(
            item -> {
              item.setCode(code);
              item.setName(name);
              return item;
            })
        .orElseGet(
            () -> {
              ModelEndpoint endpoint =
                  ModelEndpoint.builder()
                      .type(ModelEndpointType.GET)
                      .code(code)
                      .name(name)
                      .argument("id", FieldType.ID, true)
                      .returnType(model)
                      .model(model)
                      .delegate(BaseQueryGetDataFetcher.class)
                      .build();
              endpoint.getReturnType().setEndpoint(endpoint);
              return endpoint;
            });
  }

  private ModelEndpoint buildFindAllEndpoint(Model model) {
    String code = StringUtil.lowerCaseFirst(this.pluralize(model.getCode()));
    String name = "查询" + model.getName();
    return model.getEndpoints().stream()
        .filter(item -> item.getType() == ModelEndpointType.LIST)
        .findFirst()
        .map(
            item -> {
              item.setCode(code);
              item.setName(name);
              return item;
            })
        .orElseGet(
            () -> {
              ModelEndpoint endpoint =
                  ModelEndpoint.builder()
                      .type(ModelEndpointType.LIST)
                      .code(code)
                      .name(name)
                      .argument("where", getWhereInputTypeName(model), "查询条件", "{}")
                      .argument("offset", FieldType.Int, "开始位置", 0)
                      .argument("limit", FieldType.Int, "数据量", 15)
                      .argument("orderBy", getOrderByTypeName(model), "排序")
                      .returnType(true, model)
                      .model(model)
                      .delegate(BaseQueryFindAllDataFetcher.class)
                      .build();
              endpoint.getReturnType().setEndpoint(endpoint);
              return endpoint;
            });
  }

  private ModelEndpoint buildFindPaginationEndpoint(Model model) {
    String code = StringUtil.lowerCaseFirst(this.pluralize(model.getCode())) + "Connection";
    String name = model.getName() + "分页查询";
    return model.getEndpoints().stream()
        .filter(item -> item.getType() == ModelEndpointType.CONNECTION)
        .findFirst()
        .map(
            item -> {
              item.setCode(code);
              item.setName(name);
              return item;
            })
        .orElseGet(
            () -> {
              ModelEndpoint endpoint =
                  ModelEndpoint.builder()
                      .type(ModelEndpointType.CONNECTION)
                      .code(code)
                      .name(model.getName() + "分页查询")
                      .argument("where", getWhereInputTypeName(model), "查询条件", "{}")
                      .argument("page", FieldType.Int, "页码", 1)
                      .argument("pageSize", FieldType.Int, "每页展示条数", 15)
                      .argument("orderBy", getOrderByTypeName(model), "排序")
                      .returnType(getConnectionTypeName(model))
                      .delegate(BaseQueryConnectionDataFetcher.class)
                      .model(model)
                      .build();
              endpoint.getReturnType().setEndpoint(endpoint);
              return endpoint;
            });
  }

  public String pluralize(String word) {
    for (RuleAndReplacement rar : plurals) {
      String rule = rar.getRule();
      String replacement = rar.getReplacement();
      // Return if we find a match.
      Matcher matcher = Pattern.compile(rule, Pattern.CASE_INSENSITIVE).matcher(word);
      if (matcher.find()) {
        return matcher.replaceAll(replacement);
      }
    }
    return word;
  }

  public void plural(String rule, String replacement) {
    this.plurals.add(0, new RuleAndReplacement(rule, replacement));
  }

  @Override
  public void afterPropertiesSet() {
    plural("$", "s");
    plural("s$", "s");
    plural("(ax|test)is$", "$1es");
    plural("(octop|vir)us$", "$1i");
    plural("(alias|status)$", "$1es");
    plural("(bu)s$", "$1es");
    plural("(buffal|tomat)o$", "$1oes");
    plural("([ti])um$", "$1a");
    plural("sis$", "ses");
    plural("(?:([^f])fe|([lr])f)$", "$1$2ves");
    plural("(hive)$", "$1s");
    plural("([^aeiouy]|qu)y$", "$1ies");
    plural("([^aeiouy]|qu)ies$", "$1y");
    plural("(x|ch|ss|sh)$", "$1es");
    plural("(matr|vert|ind)ix|ex$", "$1ices");
    plural("([m|l])ouse$", "$1ice");
    plural("(ox)$", "$1es");
    plural("(quiz)$", "$1zes");
  }

  @Data
  @AllArgsConstructor
  static class RuleAndReplacement {
    private String rule;
    private String replacement;
  }
}
