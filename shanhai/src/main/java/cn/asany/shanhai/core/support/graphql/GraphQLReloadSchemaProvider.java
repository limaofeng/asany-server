package cn.asany.shanhai.core.support.graphql;

import graphql.kickstart.servlet.config.GraphQLSchemaServletProvider;
import java.io.IOException;

/**
 * Reload Schema Provider
 *
 * @author limaofeng
 */
public interface GraphQLReloadSchemaProvider extends GraphQLSchemaServletProvider {

  /**
   * 更新 Schema
   *
   * @throws IOException 异常
   */
  void updateSchema() throws IOException;
}
