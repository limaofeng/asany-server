package cn.asany.shanhai.data.engine;

import cn.asany.shanhai.data.domain.DataSourceConfig;
import cn.asany.shanhai.data.service.DataSourceService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfantasy.framework.util.common.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultDataSourceLoader implements IDataSourceLoader {

  private final Map<String, IDataSourceBuilder<IDataSourceOptions>> builders = new HashMap<>();

  private DataSourceService dataSourceService;

  @Override
  public List<String> getTypes() {
    return new ArrayList<>(builders.keySet());
  }

  @Override
  public void addBuilder(String type, IDataSourceBuilder<IDataSourceOptions> builder) {
    builders.put(type, builder);
  }

  @Override
  public IDataSource load(Long id) {
    DataSourceConfig config = dataSourceService.getConfig(id);
    IDataSourceBuilder<IDataSourceOptions> builder = this.builders.get(config.getType());
    Class<IDataSourceOptions> optionsClass =
        ClassUtil.getInterfaceGenricType(builder.getClass(), IDataSourceBuilder.class, 0);
    return builder.build(
        config.getId().toString(),
        config.getName(),
        config.getDescription(),
        config.getOptions(optionsClass));
  }

  @SuppressWarnings("unused")
  public void setDataSourceService(@Autowired DataSourceService dataSourceService) {
    this.dataSourceService = dataSourceService;
  }
}
