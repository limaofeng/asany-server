package cn.asany.ui.library.graphql;

import cn.asany.ui.library.bean.Oplog;
import cn.asany.ui.library.graphql.input.OplogFilter;
import cn.asany.ui.library.service.OplogService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Component;

@Component
public class OplogGraphQLQueryResolver implements GraphQLQueryResolver {

  private final OplogService oplogService;

  public OplogGraphQLQueryResolver(OplogService oplogService) {
    this.oplogService = oplogService;
  }

  public List<Oplog> oplogs(OplogFilter filter) {
    filter = ObjectUtil.defaultValue(filter, new OplogFilter());
    return this.oplogService.oplogs(filter.build());
  }
}
