package cn.asany.ui.library.graphql;

import cn.asany.ui.library.domain.Oplog;
import cn.asany.ui.library.graphql.input.OplogWhereInput;
import cn.asany.ui.library.service.OplogService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OplogGraphQLQueryResolver implements GraphQLQueryResolver {

  private final OplogService oplogService;

  public OplogGraphQLQueryResolver(OplogService oplogService) {
    this.oplogService = oplogService;
  }

  public List<Oplog> oplogs(OplogWhereInput where) {
    return this.oplogService.oplogs(where.toFilter());
  }
}
