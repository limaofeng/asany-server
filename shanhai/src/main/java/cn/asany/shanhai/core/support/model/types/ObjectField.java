package cn.asany.shanhai.core.support.model.types;

import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import cn.asany.shanhai.core.support.model.FieldType;
import lombok.Data;
import org.jfantasy.framework.dao.jpa.PropertyFilter;

@Data
public class ObjectField implements FieldType {
  private String id;
  private String name;
  private String description;

  public ObjectField(String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  @Override
  public FieldTypeFamily getFamily() {
    return null;
  }

  @Override
  public String getJavaType(ModelFieldMetadata metadata) {
    return this.id;
  }

  @Override
  public String getGraphQLType() {
    return this.id;
  }

  @Override
  public PropertyFilter.MatchType[] filters() {
    return new PropertyFilter.MatchType[] {};
  }
}
