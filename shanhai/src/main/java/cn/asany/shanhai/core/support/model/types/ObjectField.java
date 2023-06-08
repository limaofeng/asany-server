package cn.asany.shanhai.core.support.model.types;

import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import cn.asany.shanhai.core.support.model.FieldType;
import lombok.Data;
import org.jfantasy.framework.dao.MatchType;
import org.jfantasy.framework.dao.jpa.PropertyFilter;

@Data
public class ObjectField implements FieldType<Object, Object> {
  private String id;
  private String name;
  private String description;

  private String javaType;

  public ObjectField(String id, String name, String description, String javaType) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.javaType = javaType;
  }

  @Override
  public FieldTypeFamily getFamily() {
    return null;
  }

  @Override
  public String getJavaType(ModelFieldMetadata metadata) {
    return this.javaType;
  }

  @Override
  public String getGraphQLType() {
    return this.id;
  }

  @Override
  public MatchType[] filters() {
    return new MatchType[] {};
  }
}
