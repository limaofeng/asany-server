package cn.asany.shanhai.core.graphql.inputs;

import cn.asany.shanhai.core.domain.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.WhereInput;

/**
 * 模型筛选
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ModelWhereInput extends WhereInput<ModelWhereInput, Model> {

  private void setDatabaseTableName(String value) {
    this.filter.equal("metadata.databaseTableName", value);
  }
}
