package cn.asany.shanhai.core.support.model.features;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelEndpoint;
import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.bean.ModelMetadata;
import cn.asany.shanhai.core.bean.enums.ModelEndpointType;
import cn.asany.shanhai.core.bean.enums.ModelType;
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
    public List<ModelEndpoint> getEndpoints(ModelMetadata metadata) {
        List<ModelEndpoint> endpoints = new ArrayList<>();
        endpoints.add(buildCreateEndpoint(metadata));
        endpoints.add(buildUpdateEndpoint(metadata));
        endpoints.add(buildGetEndpoint(metadata));
        endpoints.add(buildFindPagerEndpoint(metadata));
        return endpoints;
    }

    public Model buildInputType(String code, String name, List<ModelField> fields) {
        return Model.builder().type(ModelType.INPUT).code(code).name(name).fields(
            fields.stream().filter(item -> !item.getIsPrimaryKey()).map(item ->
                ModelField.builder()
                    .code(item.getCode())
                    .name(item.getName())
                    .type(item.getType())
                    .isPrimaryKey(item.getIsPrimaryKey())
                    .build())
                .collect(Collectors.toList())
        ).build();
    }

    @Override
    public List<Model> getInputTypes(Model model) {
        Model inputTypeOfCreate = this.buildInputType(model.getCode() + "CreateInput", model.getName() + "录入对象", model.getFields());
        Model inputTypeOfUpdate = this.buildInputType(model.getCode() + "UpdateInput", model.getName() + "更新对象", model.getFields());
        return new ArrayList<>(Arrays.asList(inputTypeOfCreate, inputTypeOfUpdate));
    }

    private ModelEndpoint buildCreateEndpoint(ModelMetadata metadata) {
        ModelEndpoint endpoint = ModelEndpoint.builder()
            .type(ModelEndpointType.MUTATION)
            .code("create" + StringUtil.upperCaseFirst(metadata.getModel().getCode()))
            .name("新增" + metadata.getModel().getName())
//            .arguments(ModelEndpointArgument.builder().name("").type("").build())
            .returnType(metadata.getModel())
            .model(metadata.getModel())
            .build();
        endpoint.getReturnType().setEndpoint(endpoint);
        return endpoint;
    }

    private ModelEndpoint buildUpdateEndpoint(ModelMetadata metadata) {
        ModelEndpoint endpoint = ModelEndpoint.builder()
            .type(ModelEndpointType.MUTATION)
            .code("update" + StringUtil.upperCaseFirst(metadata.getModel().getCode()))
            .name("修改" + metadata.getModel().getName())
            .returnType(metadata.getModel())
            .model(metadata.getModel())
            .build();
        endpoint.getReturnType().setEndpoint(endpoint);
        return endpoint;
    }

    private ModelEndpoint buildGetEndpoint(ModelMetadata metadata) {
        ModelEndpoint endpoint = ModelEndpoint.builder()
            .type(ModelEndpointType.MUTATION)
            .code(StringUtil.lowerCaseFirst(this.pluralize(metadata.getModel().getCode())))
            .name("获取" + metadata.getModel().getName())
            .returnType(metadata.getModel())
            .model(metadata.getModel())
            .build();
        endpoint.getReturnType().setEndpoint(endpoint);
        return endpoint;
    }

    private ModelEndpoint buildFindPagerEndpoint(ModelMetadata metadata) {
        ModelEndpoint endpoint = ModelEndpoint.builder()
            .type(ModelEndpointType.MUTATION)
            .code(StringUtil.lowerCaseFirst(metadata.getModel().getCode()))
            .name(metadata.getModel().getName() + "分页查询")
            .returnType(metadata.getModel())
            .model(metadata.getModel())
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
