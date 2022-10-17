package cn.asany.shanhai.core.support.graphql;

import graphql.kickstart.servlet.config.GraphQLSchemaServletProvider;
import java.io.IOException;

public interface GraphQLReloadSchemaProvider extends GraphQLSchemaServletProvider {

  void updateSchema() throws IOException;
}
