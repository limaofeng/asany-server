package cn.asany.shanhai.core.support.model.types;

import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import cn.asany.shanhai.core.support.model.FieldType;
import lombok.Data;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Component;

@Data
@Component
public class StringField implements FieldType {
  private String id = "String";
  private String name = "字符串";
  private String javaType = java.lang.String.class.getName();
  private String graphQLType = "String";

  @Override
  public String getJavaType(ModelFieldMetadata metadata) {
    return this.javaType;
  }

  @Override
  public String getGraphQLType(ModelFieldMetadata metadata) {
    return graphQLType;
  }

  @Override
  public PropertyFilter.MatchType[] filters() {
    return new PropertyFilter.MatchType[] {
      PropertyFilter.MatchType.EQ,
      PropertyFilter.MatchType.CONTAINS,
      PropertyFilter.MatchType.STARTS_WITH,
      PropertyFilter.MatchType.ENDS_WITH
    };
  }
}
