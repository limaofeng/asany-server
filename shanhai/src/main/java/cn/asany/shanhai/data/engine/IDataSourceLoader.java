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
   * @return 返回全部支持的类型
   */
  List<String> getTypes();

  /**
   * 添加 DataSource 构造器
   *
   * @param type 类型
   * @param builder 构造器
   */
  void addBuilder(String type, IDataSourceBuilder<IDataSourceOptions> builder);

  /**
   * 通过配置加载 DataSource 实例
   *
   * @param id ID
   * @return IDataSource
   */
  IDataSource load(Long id);
}
