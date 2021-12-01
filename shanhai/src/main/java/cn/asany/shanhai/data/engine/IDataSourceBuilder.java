package cn.asany.shanhai.data.engine;

/**
 * DataSource 构造器
 *
 * @author limaofeng
 */
public interface IDataSourceBuilder<O extends IDataSourceOptions> {

  /**
   * 类型
   *
   * @return String
   */
  String type();

  /**
   * 构建 数据源
   *
   * @param id ID
   * @param name 名称
   * @param description 描述
   * @param options 配置选项
   * @return IDataSource
   */
  IDataSource build(String id, String name, String description, O options);
}
