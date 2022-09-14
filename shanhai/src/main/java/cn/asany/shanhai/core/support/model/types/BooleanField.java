package cn.asany.shanhai.core.support.model.types;

import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import cn.asany.shanhai.core.support.model.FieldType;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 布尔类型
 *
 * @author limaofeng
 */
@Data
@Component
public class BooleanField implements FieldType<Boolean, Boolean> {
  private String id = "Boolean";
  private String name = "布尔";
  private String javaType = Boolean.class.getName();
  private String graphQLType = "Boolean";
  private String description;

  private FieldTypeFamily family;

  public BooleanField() {}

  public BooleanField(FieldTypeFamily family, String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.family = family;
    this.description = description;
  }

  @Override
  public java.lang.String getJavaType(ModelFieldMetadata metadata) {
    return this.getJavaType();
  }

  @Override
  public java.lang.String getGraphQLType() {
    return this.graphQLType;
  }
}
