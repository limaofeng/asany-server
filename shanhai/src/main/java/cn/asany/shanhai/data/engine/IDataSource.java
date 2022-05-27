package cn.asany.shanhai.data.engine;

import cn.asany.shanhai.data.domain.DataSetConfig;
import java.util.List;
import java.util.Map;

/**
 * 数据源接口
 *
 * @author limaofeng
 */
public interface IDataSource {
  /**
   * 唯一ID
   *
   * @return ID
   */
  String getId();

  /**
   * 名称
   *
   * @return Name
   */
  String getName();

  /**
   * 类型
   *
   * @return Type
   */
  String getType();

  /**
   * 描述
   *
   * @return Description
   */
  String getDescription();

  /**
   * 详细配置
   *
   * @return Options
   */
  IDataSourceOptions getOptions();

  /**
   * 数据结构 (表 / 实体)
   *
   * @return List<ISchema>
   */
  List<ISchema> getSchemas();

  /**
   * 获取结构
   *
   * @param id ID
   * @return ISchema
   */
  ISchema getSchema(String id);

  /**
   * 加载数据集
   *
   * @param config 配置
   * @param variables
   * @param <T>
   * @return DataSet<T>
   */
  <T> DataSet<T> dataset(DataSetConfig config, Map<String, String> variables);
}
