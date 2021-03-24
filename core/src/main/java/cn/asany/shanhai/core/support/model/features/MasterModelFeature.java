package cn.asany.shanhai.core.support.model.features;

import cn.asany.shanhai.core.bean.*;
import cn.asany.shanhai.core.bean.enums.ModelEndpointType;
import cn.asany.shanhai.core.bean.enums.ModelType;
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
        endpoints.add(buildGetEndpoint(model));
        endpoints.add(buildFindPagerEndpoint(model));
        return endpoints;
    }

    public Model buildInputType(String code, String name, List<ModelField> fields) {
        return Model.builder().type(ModelType.INPUT).code(code).name(name).fields(
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
        return model.getCode() + "UpdateInput";
    }

    @Override
    public List<Model> getInputTypes(Model model) {
        Model inputTypeOfCreate = this.buildInputType(getCreateInputTypeName(model), model.getName() + "录入对象", model.getFields());
        Model inputTypeOfUpdate = this.buildInputType(getUpdateInputTypeName(model), model.getName() + "更新对象", model.getFields());
        return new ArrayList<>(Arrays.asList(inputTypeOfCreate, inputTypeOfUpdate));
    }

    private ModelEndpoint buildCreateEndpoint(Model model) {
        ModelEndpoint endpoint = ModelEndpoint.builder()
            .type(ModelEndpointType.MUTATION)
            .code("create" + StringUtil.upperCaseFirst(model.getCode()))
            .name("新增" + model.getName())
            .argument("input", getCreateInputTypeName(model), true)
            .returnType(model)
            .model(model)
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
            .build();
        endpoint.getReturnType().setEndpoint(endpoint);
        return endpoint;
    }

    private ModelEndpoint buildFindPagerEndpoint(Model model) {
        ModelEndpoint endpoint = ModelEndpoint.builder()
            .type(ModelEndpointType.QUERY)
            .code(StringUtil.lowerCaseFirst(this.pluralize(model.getCode())))
            .name(model.getName() + "分页查询")
            .returnType(model)
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
