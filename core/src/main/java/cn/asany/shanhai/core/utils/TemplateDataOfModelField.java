package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import lombok.AllArgsConstructor;
import org.jfantasy.framework.spring.SpringContextUtil;

@AllArgsConstructor
public class TemplateDataOfModelField {
    private ModelField field;

    public String getName() {
        return field.getName();
    }

    public String getDatabaseColumnName() {
        return field.getMetadata().getDatabaseColumnName();
    }

    public String getJavaType() {
        FieldTypeRegistry registry = SpringContextUtil.getBeanByType(FieldTypeRegistry.class);
        FieldType type = registry.getType(this.field.getType().getCode());
        return type.getJavaType(this.field.getMetadata());
    }

    public String getGraphQLType() {
        FieldTypeRegistry registry = SpringContextUtil.getBeanByType(FieldTypeRegistry.class);
        FieldType type = registry.getType(this.field.getType().getCode());
        return type.getGraphQLType(this.field.getMetadata());
    }
}