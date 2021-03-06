package cn.asany.shanhai.core.support.model.types;

import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import cn.asany.shanhai.core.support.model.FieldType;
import java.math.BigDecimal;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class FloatField implements FieldType<BigDecimal, BigDecimal> {
  private String id = "Float";
  private String name = "Float";
  private String javaType = BigDecimal.class.getName();
  private String graphQLType = "Float";

  @Override
  public java.lang.String getJavaType(ModelFieldMetadata metadata) {
    return this.getJavaType();
  }

  @Override
  public java.lang.String getGraphQLType(ModelFieldMetadata metadata) {
    return this.graphQLType;
  }
}
