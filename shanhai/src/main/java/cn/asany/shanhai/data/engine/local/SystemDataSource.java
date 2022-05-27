package cn.asany.shanhai.data.engine.local;

import cn.asany.shanhai.data.domain.DataSetConfig;
import cn.asany.shanhai.data.engine.DataSet;
import cn.asany.shanhai.data.engine.IDataSource;
import cn.asany.shanhai.data.engine.IDataSourceOptions;
import cn.asany.shanhai.data.engine.ISchema;
import java.util.List;
import java.util.Map;

/**
 * 默认 DataSource 为系统实体
 *
 * @author limaofeng
 */
public class SystemDataSource implements IDataSource {
  @Override
  public String getId() {
    return null;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String getType() {
    return null;
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public IDataSourceOptions getOptions() {
    return null;
  }

  @Override
  public List<ISchema> getSchemas() {
    return null;
  }

  @Override
  public ISchema getSchema(String id) {
    return null;
  }

  @Override
  public <T> DataSet<T> dataset(DataSetConfig config, Map<String, String> variables) {
    return null;
  }
}
