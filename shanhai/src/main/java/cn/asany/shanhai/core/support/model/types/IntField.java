package cn.asany.shanhai.core.support.model.types;

import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import cn.asany.shanhai.core.support.model.FieldType;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class IntField implements FieldType {
  private String id = "Int";
  private String name = "整数型";
  private String javaType = Long.class.getName();
  private String graphQLType = "Int";

  @Override
  public String getJavaType(ModelFieldMetadata metadata) {
    return this.javaType;
  }

  @Override
  public String getGraphQLType(ModelFieldMetadata metadata) {
    return graphQLType;
  }
}
