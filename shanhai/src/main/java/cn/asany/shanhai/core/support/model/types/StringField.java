package cn.asany.shanhai.core.support.model.types;

import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import cn.asany.shanhai.core.support.model.FieldType;
import lombok.Data;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Component;

/**
 * 字符串类型
 *
 * @author limaofeng
 */
@Data
@Component
public class StringField implements FieldType<String, String> {
  private String id = "String";
  private String name = "字符串";
  private String javaType = java.lang.String.class.getName();
  private String graphQLType = "String";

  private String description;

  private FieldTypeFamily family;

  private int index;

  public StringField() {}

  public StringField(FieldTypeFamily family, String id, String name, String description) {
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

  @Override
  public PropertyFilter.MatchType[] filters() {
    return new PropertyFilter.MatchType[] {
      PropertyFilter.MatchType.EQ,
      PropertyFilter.MatchType.CONTAINS,
      PropertyFilter.MatchType.STARTS_WITH,
      PropertyFilter.MatchType.ENDS_WITH,
      PropertyFilter.MatchType.IN,
      PropertyFilter.MatchType.NOT_IN
    };
  }
}
