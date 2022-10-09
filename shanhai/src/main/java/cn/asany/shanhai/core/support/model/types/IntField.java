package cn.asany.shanhai.core.support.model.types;

import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import cn.asany.shanhai.core.support.model.FieldType;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 整型
 *
 * @author limaofeng
 */
@Data
@Component
public class IntField implements FieldType<Long, Object> {
  private String id = "Int";
  private String name = "整数型";
  private String javaType = Long.class.getName();
  private String graphQLType = "Int";

  private String description;

  private FieldTypeFamily family;

  public IntField() {}

  public IntField(FieldTypeFamily family, String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.family = family;
    this.description = description;
  }

  @Override
  public String getJavaType(ModelFieldMetadata metadata) {
    return this.javaType;
  }

  @Override
  public String getGraphQLType() {
    return graphQLType;
  }
}
