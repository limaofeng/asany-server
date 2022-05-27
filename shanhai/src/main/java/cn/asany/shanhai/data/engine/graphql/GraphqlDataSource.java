package cn.asany.shanhai.data.engine.graphql;

import cn.asany.shanhai.data.domain.DataSetConfig;
import cn.asany.shanhai.data.engine.DataSet;
import cn.asany.shanhai.data.engine.IDataSource;
import cn.asany.shanhai.data.engine.IDataSourceOptions;
import cn.asany.shanhai.data.engine.ISchema;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.jfantasy.graphql.client.GraphQLResponse;
import org.jfantasy.graphql.client.GraphQLTemplate;

/**
 * 数据源
 *
 * @author limaofeng
 */
public class GraphqlDataSource implements IDataSource {

  private String id;
  private String name;
  private String description;
  private String type;

  private GraphQLTemplate graphqlTemplate;
  private GraphQLDataSourceOptions options;

  public GraphqlDataSource(
      String id,
      String name,
      String description,
      String type,
      GraphQLTemplate graphqlTemplate,
      GraphQLDataSourceOptions options) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.type = type;
    this.options = options;
    this.graphqlTemplate = graphqlTemplate;
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String getType() {
    return this.type;
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  @Override
  public IDataSourceOptions getOptions() {
    return this.options;
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
  @SneakyThrows
  public <T> DataSet<T> dataset(DataSetConfig config, Map<String, String> variables) {
    GraphQLDataSetOptions options = config.getOptions(GraphQLDataSetOptions.class);
    Map<String, String> mergeVars = options.getVariables();
    if (mergeVars != null) {
      mergeVars.putAll(variables);
    } else {
      mergeVars = variables;
    }
    GraphQLResponse response =
        graphqlTemplate.post(options.getGql(), options.getOperationName(), mergeVars);
    DataSet dataSet =
        DataSet.builder()
            .result(response.get("$.data." + options.getOperationName(), HashMap[].class))
            .build();
    return dataSet;
  }
}
