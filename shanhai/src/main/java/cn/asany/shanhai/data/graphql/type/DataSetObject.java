package cn.asany.shanhai.data.graphql.type;

import cn.asany.shanhai.data.domain.toy.DataSetField;
import cn.asany.shanhai.data.domain.toy.DataSetFilter;
import cn.asany.shanhai.data.engine.DataSet;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据集
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSetObject {
  /** 数据集字段 */
  private List<DataSetField> fields;
  /** 数据集筛选 */
  private List<DataSetFilter> filters;
  /** 数据结果 */
  private Object[] result;

  public static DataSetObject of(DataSet<Object> dataset) {
    return DataSetObject.builder()
        .fields(dataset.getFields())
        .filters(dataset.getFilters())
        .result(dataset.getResult())
        .build();
  }
}
