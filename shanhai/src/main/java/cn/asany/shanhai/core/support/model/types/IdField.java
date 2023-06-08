package cn.asany.shanhai.core.support.model.types;

import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import cn.asany.shanhai.core.support.model.DatabaseColumn;
import cn.asany.shanhai.core.support.model.FieldType;
import lombok.Data;
import org.jfantasy.framework.dao.MatchType;
import org.springframework.stereotype.Component;

/**
 * 主键
 *
 * @author limaofeng
 */
@Data
@Component
public class IdField implements FieldType<Long, Long> {
  private String id = "ID";
  private String name = "ID";
  private String javaType = Long.class.getName();
  private String graphQLType = "ID";

  private String description;

  private FieldTypeFamily family;

  @Override
  public String getJavaType(ModelFieldMetadata metadata) {
    return this.javaType;
  }

  @Override
  public String getGraphQLType() {
    return graphQLType;
  }

  @Override
  public DatabaseColumn getColumn(ModelFieldMetadata metadata) {
    return DatabaseColumn.builder()
        .name(metadata.getDatabaseColumnName())
        .updatable(false)
        .nullable(false)
        .build();
  }

  @Override
  public MatchType[] filters() {
    return new MatchType[] {MatchType.EQ, MatchType.IN, MatchType.NOT_IN};
  }
}
