package cn.asany.shanhai.core.support.model.types;

import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import cn.asany.shanhai.core.support.model.FieldType;
import java.math.BigDecimal;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 浮点型
 *
 * @author limaofeng
 */
@Data
@Component
public class FloatField implements FieldType<BigDecimal, BigDecimal> {
  private String id = "Float";
  private String name = "Float";
  private String javaType = BigDecimal.class.getName();
  private String graphQLType = "Float";

  private String description;

  private FieldTypeFamily family;

  public FloatField() {}

  public FloatField(FieldTypeFamily family, String id, String name, String description) {
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
