package cn.asany.shanhai.data.graphql;

import cn.asany.shanhai.data.domain.DataSetConfig;
import cn.asany.shanhai.data.engine.IDataSource;
import cn.asany.shanhai.data.engine.IDataSourceLoader;
import cn.asany.shanhai.data.graphql.type.DataSetObject;
import cn.asany.shanhai.data.service.DataSetService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * DataSet
 *
 * @author limaofeng
 */
@Component
public class DataSetGraphQLQueryResolver implements GraphQLQueryResolver {

  private final DataSetService dataSetService;
  private final IDataSourceLoader dataSourceLoader;

  public DataSetGraphQLQueryResolver(
      IDataSourceLoader dataSourceLoader, DataSetService dataSetService) {
    this.dataSourceLoader = dataSourceLoader;
    this.dataSetService = dataSetService;
  }

  public DataSetObject dataset(Long id, Map<String, String> params) {
    DataSetConfig config = this.dataSetService.getConfig(id);
    IDataSource dataSource = this.dataSourceLoader.load(config.getDatasource().getId());
    return DataSetObject.of(dataSource.dataset(config, params));
  }
}
