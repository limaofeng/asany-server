package cn.asany.shanhai.data.engine.graphql;

import cn.asany.shanhai.data.engine.IDataSource;
import cn.asany.shanhai.data.engine.IDataSourceBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.graphql.client.GraphQLTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * GraphQL DataSource
 *
 * @author limaofeng
 */
@Component
public class GraphQLDataSourceBuilder implements IDataSourceBuilder<GraphQLDataSourceOptions> {

  public static final String GRAPHQL = "graphql";

  private final ResourceLoader resourceLoader;
  private GraphQLTemplate graphQLTemplate;

  public GraphQLDataSourceBuilder(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @Override
  public String type() {
    return GRAPHQL;
  }

  @Override
  public IDataSource build(
      String id, String name, String description, GraphQLDataSourceOptions options) {
    RestTemplate restTemplate = SpringBeanUtils.getBean(RestTemplate.class);
    ObjectMapper objectMapper = SpringBeanUtils.getBean(ObjectMapper.class);
    GraphQLTemplate graphQLTemplate =
        new GraphQLTemplate(this.resourceLoader, restTemplate, options.getUrl(), objectMapper);
    return new GraphqlDataSource(id, name, description, GRAPHQL, graphQLTemplate, options);
  }
}
