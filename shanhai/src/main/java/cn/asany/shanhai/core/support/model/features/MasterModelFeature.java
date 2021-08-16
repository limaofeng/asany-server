package cn.asany.shanhai.core.support.model.features;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelEndpoint;
import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.bean.enums.ModelEndpointType;
import cn.asany.shanhai.core.bean.enums.ModelType;
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
import org.jfantasy.framework.dao.jpa.PropertyFilter.MatchType;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class MasterModelFeature implements IModelFeature, InitializingBean {
  public static final String ID = "master";
  private String id = ID;

  @Autowired private FieldTypeRegistry fieldTypeRegistry;

  private List<RuleAndReplacement> plurals = new ArrayList<>();

  @Override
  public List<ModelEndpoint> getEndpoints(Model model) {
    List<ModelEndpoint> endpoints = new ArrayList<>();
    endpoints.add(buildCreateEndpoint(model));
    endpoints.add(buildUpdateEndpoint(model));
    endpoints.add(buildDeleteEndpoint(model));
    endpoints.add(buildGetEndpoint(model));
    endpoints.add(buildFindAllEndpoint(model));
    endpoints.add(buildFindPaginationEndpoint(model));
    return endpoints;
  }

  private Set<ModelField> cloneObjectFields(Set<ModelField> fields) {
    return cloneModelFields(fields, item -> true);
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
                    .type(item.getType())
                    .build())
        .collect(Collectors.toSet());
  }

  private Model buildType(ModelType type, String code, String name, Set<ModelField> fields) {
    return Model.builder().type(type).code(code).name(name).fields(fields).build();
  }

  private static String getCreateInputTypeName(Model model) {
    return model.getCode() + "CreateInput";
  }

  private static String getUpdateInputTypeName(Model model) {
    return model.getCode() + "UpdateInput";
  }

  private static String getWhereInputTypeName(Model model) {
    return model.getCode() + "WhereInput";
  }

  private static String getOrderByTypeName(Model model) {
    return model.getCode() + "OrderBy";
  }

  private static String getConnectionTypeName(Model model) {
    return model.getCode() + "Connection";
  }

  private Set<ModelField> buildWhereFields(Model model) {
    Set<ModelField> fields = new HashSet<>();
    fields.add(
        ModelField.builder()
            .code("AND")
            .type(getWhereInputTypeName(model))
            .list(true)
            .name("Logical AND on all given filters.")
            .build());
    fields.add(
        ModelField.builder()
            .code("OR")
            .type(getWhereInputTypeName(model))
            .list(true)
            .name("Logical OR on all given filters.")
            .build());
    fields.add(
        ModelField.builder()
            .code("NOT")
            .type(getWhereInputTypeName(model))
            .list(true)
            .name("Logical NOT on all given filters combined by AND.")
            .build());
    for (ModelField field :
        model.getFields().stream()
            .filter(item -> !item.getSystem() && !item.getPrimaryKey())
            .collect(Collectors.toList())) {
      if (field.getType().getType() == ModelType.SCALAR) {
        for (MatchType matchType : field.getMetadata().getFilters()) {
          String fieldName =
              matchType == MatchType.EQ
                  ? field.getCode()
                  : field.getCode() + "_" + matchType.getSlug();
          fields.add(ModelField.builder().code(fieldName).type(field.getType()).build());
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
            .type(model)
            .required(true)
            .name("The item at the end of the edge.")
            .build());
    fields.add(
        ModelField.builder()
            .code("cursor")
            .type(FieldType.String)
            .required(true)
            .name("A cursor for use in pagination.")
            .build());
    return fields;
  }

  private static Set<ModelField> buildOrderByFields(Model model) {
    Set<ModelField> fields = new HashSet<>();
    for (ModelField field :
        model.getFields().stream()
            .filter(item -> !item.getPrimaryKey())
            .collect(Collectors.toList())) {
      fields.add(
          ModelField.builder()
              .code(field.getCode() + "_ASC")
              .name(field.getName() + " 升序")
              .build());
      fields.add(
          ModelField.builder()
              .code(field.getCode() + "_DESC")
              .name(field.getName() + " 降序")
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

  @Override
  public List<Model> getTypes(Model model) {
    Model inputTypeOfCreate =
        this.buildType(
            ModelType.INPUT_OBJECT,
            getCreateInputTypeName(model),
            model.getName() + "录入对象",
            cloneInputFields(model.getFields()));
    Model inputTypeOfUpdate =
        this.buildType(
            ModelType.INPUT_OBJECT,
            getUpdateInputTypeName(model),
            model.getName() + "更新对象",
            cloneInputFields(model.getFields()));
    Model inputTypeOfFilter =
        this.buildType(
            ModelType.INPUT_OBJECT,
            getWhereInputTypeName(model),
            model.getName() + "过滤器",
            buildWhereFields(model));
    Model inputTypeOfOrderBy =
        this.buildType(
            ModelType.ENUM,
            getOrderByTypeName(model),
            model.getName() + "排序",
            buildOrderByFields(model));
    Model typeOfEdge =
        this.buildType(
            ModelType.OBJECT,
            getEdgeTypeName(model),
            model.getName() + " A connection to a list of items.",
            buildEdgeFields(model));
    Model typeOfConnection =
        this.buildType(
            ModelType.OBJECT,
            getConnectionTypeName(model),
            model.getName() + " 分页对象",
            buildConnectionFields(model));
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
    return model.getCode() + "Edge";
  }

  private ModelEndpoint buildCreateEndpoint(Model model) {
    ModelEndpoint endpoint =
        ModelEndpoint.builder()
            .type(ModelEndpointType.MUTATION)
            .code("create" + StringUtil.upperCaseFirst(model.getCode()))
            .name("新增" + model.getName())
            .argument("input", getCreateInputTypeName(model), true)
            .returnType(model)
            .model(model)
            .delegate(BaseMutationCreateDataFetcher.class)
            .build();
    endpoint.getReturnType().setEndpoint(endpoint);
    return endpoint;
  }

  private ModelEndpoint buildUpdateEndpoint(Model model) {
    ModelEndpoint endpoint =
        ModelEndpoint.builder()
            .type(ModelEndpointType.MUTATION)
            .code("update" + StringUtil.upperCaseFirst(model.getCode()))
            .name("修改" + model.getName())
            .argument("id", FieldType.ID.getCode(), true)
            .argument("input", getUpdateInputTypeName(model), true)
            .argument("merge", FieldType.Boolean.getCode(), "启用合并模式", true)
            .returnType(model)
            .model(model)
            .delegate(BaseMutationUpdateDataFetcher.class)
            .build();
    endpoint.getReturnType().setEndpoint(endpoint);
    return endpoint;
  }

  private ModelEndpoint buildDeleteEndpoint(Model model) {
    ModelEndpoint endpoint =
        ModelEndpoint.builder()
            .type(ModelEndpointType.MUTATION)
            .code("delete" + StringUtil.upperCaseFirst(model.getCode()))
            .name("删除" + model.getName())
            .argument("id", FieldType.ID.getCode(), true)
            .returnType(model)
            .model(model)
            .delegate(BaseMutationDeleteDataFetcher.class)
            .build();
    endpoint.getReturnType().setEndpoint(endpoint);
    return endpoint;
  }

  private ModelEndpoint buildGetEndpoint(Model model) {
    ModelEndpoint endpoint =
        ModelEndpoint.builder()
            .type(ModelEndpointType.QUERY)
            .code(StringUtil.lowerCaseFirst(model.getCode()))
            .name("获取" + model.getName())
            .argument("id", FieldType.ID.getCode(), true)
            .returnType(model)
            .model(model)
            .delegate(BaseQueryGetDataFetcher.class)
            .build();
    endpoint.getReturnType().setEndpoint(endpoint);
    return endpoint;
  }

  private ModelEndpoint buildFindAllEndpoint(Model model) {
    ModelEndpoint endpoint =
        ModelEndpoint.builder()
            .type(ModelEndpointType.QUERY)
            .code(StringUtil.lowerCaseFirst(this.pluralize(model.getCode())))
            .name("查询" + model.getName())
            .argument("where", getWhereInputTypeName(model))
            .argument("first", FieldType.Int, "开始位置")
            .argument("offset", FieldType.Int, "偏移量")
            .argument("orderBy", getOrderByTypeName(model), "排序")
            .returnType(true, model)
            .model(model)
            .delegate(BaseQueryFindAllDataFetcher.class)
            .build();
    endpoint.getReturnType().setEndpoint(endpoint);
    return endpoint;
  }

  private ModelEndpoint buildFindPaginationEndpoint(Model model) {
    ModelEndpoint endpoint =
        ModelEndpoint.builder()
            .type(ModelEndpointType.QUERY)
            .code(StringUtil.lowerCaseFirst(this.pluralize(model.getCode())) + "Connection")
            .name(model.getName() + "分页查询")
            .argument("where", getWhereInputTypeName(model))
            .argument("page", FieldType.Int, "页码", 1)
            .argument("pageSize", FieldType.Int, "每页展示条数", 15)
            .argument("orderBy", getOrderByTypeName(model), "排序")
            .returnType(getConnectionTypeName(model))
            .model(model)
            .build();
    endpoint.getReturnType().setEndpoint(endpoint);
    return endpoint;
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
