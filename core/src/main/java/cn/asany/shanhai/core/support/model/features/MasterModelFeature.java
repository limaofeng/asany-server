package cn.asany.shanhai.core.support.model.features;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelEndpoint;
import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.bean.enums.ModelEndpointType;
import cn.asany.shanhai.core.bean.enums.ModelType;
import cn.asany.shanhai.core.support.graphql.resolvers.base.*;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.IModelFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Data
@Component
public class MasterModelFeature implements IModelFeature, InitializingBean {
    public static final String ID = "master";
    private String id = ID;

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

    private Model buildType(ModelType type, String code, String name, List<ModelField> fields) {
        return Model.builder().type(type).code(code).name(name).fields(
            fields.stream().filter(item -> !item.getPrimaryKey() && !item.getSystem()).map(item ->
                ModelField.builder()
                    .code(item.getCode())
                    .name(item.getName())
                    .type(item.getType())
                    .build())
                .collect(Collectors.toList())
        ).build();
    }

    private static String getCreateInputTypeName(Model model) {
        return model.getCode() + "CreateInput";
    }

    private static String getUpdateInputTypeName(Model model) {
        return model.getCode() + "Filter";
    }

    private static String getFilterInputTypeName(Model model) {
        return model.getCode() + "UpdateInput";
    }

    private static String getConnectionTypeName(Model model) {
        return model.getCode() + "Connection";
    }

    private static List<ModelField> buildFilterFields(Model model) {
        List<ModelField> fields = new ArrayList<>();
        for (ModelField field : model.getFields().stream().filter(item -> !item.getSystem() && !item.getPrimaryKey()).collect(Collectors.toList())) {
            fields.add(ModelField.builder().code(field.getCode()).type(field.getType()).name(field.getName()).build());
        }
        return fields;
    }

    private List<ModelField> buildEdgeFields(Model model) {
        List<ModelField> fields = new ArrayList<>();
        fields.add(ModelField.builder().code("node").type(model).required(true).name("The item at the end of the edge.").build());
        fields.add(ModelField.builder().code("cursor").type(model).required(true).name("A cursor for use in pagination.").build());
        return fields;
    }

    private static List<ModelField> buildOrderByFields(Model model) {
        List<ModelField> fields = new ArrayList<>();
        for (ModelField field : model.getFields().stream().filter(item -> !item.getPrimaryKey()).collect(Collectors.toList())) {
            fields.add(ModelField.builder().code(field.getCode() + "_ASC").name(field.getName() + " 升序").build());
            fields.add(ModelField.builder().code(field.getCode() + "_DESC").name(field.getName() + " 降序").build());
        }
        return fields;
    }

    private static List<ModelField> buildConnectionFields(Model model) {
        List<ModelField> fields = new ArrayList<>();
        fields.add(ModelField.builder().code("totalCount").type(FieldType.Int).name("总数据条数").required(true).build());
        fields.add(ModelField.builder().code("pageSize").type(FieldType.Int).name("每页数据条数").required(true).build());
        fields.add(ModelField.builder().code("totalPage").type(FieldType.Int).name("总页数").required(true).build());
        fields.add(ModelField.builder().code("currentPage").type(FieldType.Int).name("当前页码").required(true).build());
        fields.add(ModelField.builder().code("edges").type(getEdgeTypeName(model)).list(true).required(true).name("A list of edges.").build());
        return fields;
    }

    @Override
    public List<Model> getTypes(Model model) {
        Model inputTypeOfCreate = this.buildType(ModelType.INPUT, getCreateInputTypeName(model), model.getName() + "录入对象", model.getFields());
        Model inputTypeOfUpdate = this.buildType(ModelType.INPUT, getUpdateInputTypeName(model), model.getName() + "更新对象", model.getFields());
        Model inputTypeOfFilter = this.buildType(ModelType.INPUT, getFilterInputTypeName(model), model.getName() + "过滤器", buildFilterFields(model));
        Model inputTypeOfOrderBy = this.buildType(ModelType.ENUM, getFilterInputTypeName(model), model.getName() + "过滤器", buildOrderByFields(model));
        Model typeOfEdge = this.buildType(ModelType.TYPE, getEdgeTypeName(model), model.getName() + " ：A connection to a list of items.", buildEdgeFields(model));
        Model typeOfConnection = this.buildType(ModelType.TYPE, getConnectionTypeName(model), model.getName() + " ：A connection to a list of items.", buildConnectionFields(model));
        return new ArrayList<>(Arrays.asList(inputTypeOfCreate, inputTypeOfUpdate, inputTypeOfFilter, inputTypeOfOrderBy, typeOfEdge, typeOfConnection));
    }

    private static String getEdgeTypeName(Model model) {
        return model.getCode() + "Edge";
    }

    private ModelEndpoint buildCreateEndpoint(Model model) {
        ModelEndpoint endpoint = ModelEndpoint.builder()
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
        ModelEndpoint endpoint = ModelEndpoint.builder()
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
        ModelEndpoint endpoint = ModelEndpoint.builder()
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
        ModelEndpoint endpoint = ModelEndpoint.builder()
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
        ModelEndpoint endpoint = ModelEndpoint.builder()
            .type(ModelEndpointType.QUERY)
            .code(StringUtil.lowerCaseFirst(this.pluralize(model.getCode())))
            .name("查询" + model.getName())
            .returnType(true, model)
            .model(model)
            .delegate(BaseQueryFindFindAllDataFetcher.class)
            .build();
        endpoint.getReturnType().setEndpoint(endpoint);
        return endpoint;
    }

    private ModelEndpoint buildFindPaginationEndpoint(Model model) {
        ModelEndpoint endpoint = ModelEndpoint.builder()
            .type(ModelEndpointType.QUERY)
            .code(StringUtil.lowerCaseFirst(this.pluralize(model.getCode())) + "Connection")
            .name(model.getName() + "分页查询")
            .argument("filter", getFilterInputTypeName(model))
            .argument("page", FieldType.Int, "页码", 1)
            .argument("pageSize", FieldType.Int, "每页展示条数", 15)
            .argument("orderBy", FieldType.Int, "每页展示条数", 15)
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
