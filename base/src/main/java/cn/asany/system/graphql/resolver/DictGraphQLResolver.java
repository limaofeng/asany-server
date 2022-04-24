package cn.asany.system.graphql.resolver;

import cn.asany.base.utils.Hashids;
import cn.asany.system.bean.Dict;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class DictGraphQLResolver implements GraphQLResolver<Dict> {

  public String id(Dict dict) {
    return Hashids.toId(dict.getId().toString());
  }

}
