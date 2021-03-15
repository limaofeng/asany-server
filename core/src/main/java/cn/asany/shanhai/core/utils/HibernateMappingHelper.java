package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.support.FieldType;
import cn.asany.shanhai.core.support.FieldTypeRegistry;
import com.github.jknack.handlebars.Template;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.HandlebarsTemplateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HibernateMappingHelper implements InitializingBean {

    private Template template;

    @Override
    public void afterPropertiesSet() throws Exception {
        template = HandlebarsTemplateUtils.template("/hibernate-mapping");
    }

    /**
     * 生成 Hibernate Mapping XML
     *
     * @param model
     */
    @SneakyThrows
    public String generateXML(Model model) {
        return template.apply(new ModelTemplateData(model));
    }

    @AllArgsConstructor
    static class ModelTemplateData {
        private Model model;

        public String getName() {
            return model.getMetadata().getName();
        }

        public String getDatabaseTableName() {
            return model.getMetadata().getDatabaseTableName();
        }

        public ModelFieldTemplateData getIdField() {
            Optional<ModelField> idFieldOptional = ModelUtils.getId(model);
            return new ModelFieldTemplateData(idFieldOptional.get());
        }

        public List<ModelFieldTemplateData> getFields() {
            return ModelUtils.getFields(model).stream().map(item -> new ModelFieldTemplateData(item)).collect(Collectors.toList());
        }
    }

    @AllArgsConstructor
    static class ModelFieldTemplateData {
        private ModelField field;

        public String getName() {
            return field.getMetadata().getName();
        }

        public String getDatabaseColumnName() {
            return field.getMetadata().getDatabaseColumnName();
        }

        public String getJavaType() {
            FieldTypeRegistry registry = SpringContextUtil.getBeanByType(FieldTypeRegistry.class);
            FieldType type = registry.getType(this.field.getType());
            return type.getJavaType(this.field.getMetadata());
        }
    }

}
