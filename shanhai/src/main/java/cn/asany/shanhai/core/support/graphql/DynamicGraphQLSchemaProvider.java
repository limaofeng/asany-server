package cn.asany.shanhai.core.support.graphql;

import cn.asany.shanhai.core.support.ModelParser;
import graphql.kickstart.execution.config.GraphQLSchemaProvider;
import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;
import org.springframework.scheduling.annotation.Async;

/**
 * 动态 GraphQL Schema 提供者
 *
 * @author limaofeng
 */
public class DynamicGraphQLSchemaProvider implements GraphQLReloadSchemaProvider {

  private final ModelParser modelParser;
  @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
  private GraphQLSchema graphQLSchema;
  private GraphQLSchema readOnlySchema;

  public DynamicGraphQLSchemaProvider(ModelParser modelParser) {
    this.modelParser = modelParser;

    SchemaParser schemaParser = modelParser.getSchemaParser();

    this.graphQLSchema = schemaParser.makeExecutableSchema();
    this.readOnlySchema = GraphQLSchemaProvider.copyReadOnly(this.graphQLSchema);
  }

  @Async
  @Override
  public void updateSchema() throws IOException {
    SchemaParser schemaParser = modelParser.rebuildSchemaParser();

    this.graphQLSchema = schemaParser.makeExecutableSchema();
    this.readOnlySchema = GraphQLSchemaProvider.copyReadOnly(this.graphQLSchema);
  }

  @Override
  public GraphQLSchema getSchema() {
    return this.graphQLSchema;
  }

  @Override
  public GraphQLSchema getReadOnlySchema() {
    return this.readOnlySchema;
  }

  @Override
  public GraphQLSchema getSchema(HttpServletRequest request) {
    return this.graphQLSchema;
  }

  @Override
  public GraphQLSchema getSchema(HandshakeRequest request) {
    return this.graphQLSchema;
  }

  @Override
  public GraphQLSchema getReadOnlySchema(HttpServletRequest request) {
    return this.readOnlySchema;
  }
}
