package cn.asany.shanhai.data.engine;

import cn.asany.shanhai.data.bean.DataSourceConfig;
import cn.asany.shanhai.data.service.DataSourceService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jfantasy.framework.util.common.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultDataSourceLoader implements IDataSourceLoader {

  private Map<String, IDataSourceBuilder> builders = new HashMap<>();

  @Autowired private DataSourceService dataSourceService;

  @Override
  public List<String> getTypes() {
    return builders.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
  }

  @Override
  public void addBuilder(String type, IDataSourceBuilder builder) {
    builders.put(type, builder);
  }

  @Override
  public IDataSource load(Long id) {
    DataSourceConfig config = dataSourceService.getConfig(id);
    IDataSourceBuilder<IDataSourceOptions> builder = this.builders.get(config.getType());
    Class optionsClass =
        ClassUtil.getInterfaceGenricType(builder.getClass(), IDataSourceBuilder.class, 0);
    return builder.build(
        config.getId().toString(),
        config.getName(),
        config.getDescription(),
        config.getOptions(optionsClass));
  }
}
