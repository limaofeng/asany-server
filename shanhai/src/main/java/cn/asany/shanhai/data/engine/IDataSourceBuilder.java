package cn.asany.shanhai.data.engine;

/**
 * DataSource 构造器
 *
 * @author limaofeng
 */
public interface IDataSourceBuilder {

  String type();

  IDataSource build(IDataSourceOptions options);
}
