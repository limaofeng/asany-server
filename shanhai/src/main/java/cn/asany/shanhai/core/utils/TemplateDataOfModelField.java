package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.bean.enums.ModelType;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import lombok.AllArgsConstructor;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.framework.util.common.ObjectUtil;

@AllArgsConstructor
public class TemplateDataOfModelField {
  private ModelField field;

  public String getCode() {
    return field.getCode();
  }

  public String getName() {
    return field.getName();
  }

  public String getDatabaseColumnName() {
    return field.getMetadata().getDatabaseColumnName();
  }

  public String getJavaType() {
    FieldTypeRegistry registry = SpringBeanUtils.getBeanByType(FieldTypeRegistry.class);
    FieldType type = registry.getType(this.field.getType().getCode());
    return type.getJavaType(this.field.getMetadata());
  }

  public boolean isNonInsertable() {
    return !ObjectUtil.defaultValue(field.getMetadata().getInsertable(), Boolean.TRUE);
  }

  public boolean isNonUpdatable() {
    return !ObjectUtil.defaultValue(field.getMetadata().getUpdatable(), Boolean.TRUE);
  }

  public String getHibernateType() {
    FieldTypeRegistry registry = SpringBeanUtils.getBeanByType(FieldTypeRegistry.class);
    FieldType type = registry.getType(this.field.getType().getCode());
    return type.getHibernateType(this.field.getMetadata());
  }

  public String getGraphQLType() {
    ModelUtils modelUtils = ModelUtils.getInstance();
    Model modelType = modelUtils.getModelById(this.field.getType().getId()).get();
    String graphQLType = modelType.getCode();
    if (modelType.getType() == ModelType.SCALAR) {
      FieldTypeRegistry registry = SpringBeanUtils.getBeanByType(FieldTypeRegistry.class);
      FieldType type = registry.getType(modelType.getCode());
      graphQLType = type == null ? graphQLType : type.getGraphQLType(this.field.getMetadata());
    }
    Boolean isList = this.field.getList();
    return isList ? "[" + graphQLType + "]" : graphQLType;
  }

  public Boolean getRequired() {
    return this.field.getRequired();
  }

  public String getType() {
    return getGraphQLType();
  }
}
