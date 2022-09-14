package cn.asany.shanhai.core.support.model.types;

import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import cn.asany.shanhai.core.support.model.FieldType;
import lombok.Getter;

@Getter
public class ReferenceField implements FieldType<Long, Long> {
  private String id = "Reference";
  private String name = "Reference";
  private String description;
  private FieldTypeFamily family;

  public ReferenceField() {}

  public ReferenceField(FieldTypeFamily family, String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.family = family;
    this.description = description;
  }

  @Override
  public String getJavaType(ModelFieldMetadata metadata) {
    return "Unknown";
  }

  @Override
  public String getGraphQLType() {
    return "Unknown";
  }

  @Override
  public Long convertToEntityAttribute(Long dbData) {
    return null;
  }
}
