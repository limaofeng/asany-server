package cn.asany.shanhai.data.engine;

import cn.asany.shanhai.data.domain.DataSetConfig;

/**
 * @author limaofeng
 * @date 2022/7/28 9:12 9:12
 */
public interface ISchema<T, PK> {
  /**
   * ID
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
   * 获取字段
   *
   * @return Field[]
   */
  Field[] getFields();

  /**
   * 获取字段
   *
   * @param key 字段 Key
   * @return Field
   */
  Field getField(String key);

  /**
   * 获取主键字段
   *
   * @return Field
   */
  Field getPrimaryKey();

  /**
   * 加载数据集
   *
   * @param config 配置
   * @return DataSet
   */
  DataSet<T> load(DataSetConfig config);

  /**
   * 加载数据集
   *
   * @param config 配置
   * @return DataSet
   */
  DataSet<T> load(DataSetConfig config, int offset, int limit);

  /**
   * 查询单个对象
   *
   * @param id
   * @return T
   */
  T get(PK id);

  /**
   * 插入对象
   *
   * @param object
   * @return T
   */
  PK insert(T object);

  /**
   * 更新对象
   *
   * @param object
   * @return T
   */
  void update(T object);

  /**
   * 删除对象
   *
   * @param object
   */
  void delete(T object);
}
