package cn.asany.shanhai.data.engine;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据集
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSet<T> {
  List<T> result;
}
