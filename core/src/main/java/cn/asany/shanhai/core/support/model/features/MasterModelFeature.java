package cn.asany.shanhai.core.support.model.features;

import cn.asany.shanhai.core.bean.ModelEndpoint;
import cn.asany.shanhai.core.bean.ModelMetadata;
import cn.asany.shanhai.core.support.model.IModelFeature;
import lombok.Data;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Component
public class MasterModelFeature implements IModelFeature, InitializingBean {
    public static final String ID = "master";
    private String id = ID;

    private List<RuleAndReplacement> plurals = new ArrayList<RuleAndReplacement>();

    @Override
    public List<ModelEndpoint> getEndpoints(ModelMetadata metadata) {
        List<ModelEndpoint> endpoints = new ArrayList<>();
        endpoints.add(ModelEndpoint.builder().name("create" + StringUtil.upperCaseFirst(metadata.getName())).build());
        endpoints.add(ModelEndpoint.builder().name("update" + StringUtil.upperCaseFirst(metadata.getName())).build());
        endpoints.add(ModelEndpoint.builder().name(this.pluralize(metadata.getName())).build());
        endpoints.add(ModelEndpoint.builder().name(metadata.getName()).build());
        return endpoints;
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
    public void afterPropertiesSet() throws Exception {
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

    static class RuleAndReplacement {
        private String rule;
        private String replacement;

        public RuleAndReplacement(String rule, String replacement) {
            this.rule = rule;
            this.replacement = replacement;
        }

        public String getReplacement() {
            return replacement;
        }

        public void setReplacement(String replacement) {
            this.replacement = replacement;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }
    }
}
