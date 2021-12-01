package cn.asany.shanhai.data.engine;

import java.util.List;

/**
 * DataSource 加载器
 *
 * @author limaofeng
 */
public interface IDataSourceLoader {
  /**
   * 支持的 DataSource 类型列表
   *
   * @return
   */
  List<String> getTypes();

  /**
   * 添加 DataSource 构造器
   *
   * @param type
   * @param builder
   */
  void addBuilder(String type, IDataSourceBuilder builder);

  /**
   * 通过配置加载 DataSource 实例
   *
   * @param id ID
   * @return IDataSource
   */
  IDataSource load(Long id);
}
