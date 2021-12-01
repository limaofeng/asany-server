package cn.asany.shanhai.data.engine.local;

import cn.asany.shanhai.data.engine.IDataSource;
import cn.asany.shanhai.data.engine.IDataSourceBuilder;
import cn.asany.shanhai.data.engine.IDataSourceOptions;
import org.springframework.stereotype.Component;

/**
 * 系统默认 DataSource
 *
 * @author limaofeng
 */
@Component
public class SystemDataSourceBuilder implements IDataSourceBuilder {

  @Override
  public String type() {
    return null;
  }

  @Override
  public IDataSource build(String id, String name, String description, IDataSourceOptions options) {
    return null;
  }
}
