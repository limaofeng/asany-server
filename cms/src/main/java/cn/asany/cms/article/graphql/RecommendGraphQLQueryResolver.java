package cn.asany.cms.article.graphql;

import cn.asany.cms.article.bean.Recommend;
import cn.asany.cms.article.graphql.input.RecommendFilter;
import cn.asany.cms.article.service.RecommendService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @Description @Author ChenWenJie @Data 2020/10/22 11:21 上午 */
@Component
public class RecommendGraphQLQueryResolver implements GraphQLQueryResolver {
  @Autowired private RecommendService service;

  public List<Recommend> recommends(String organization, RecommendFilter filter, OrderBy orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new RecommendFilter()).getBuilder();
    builder.equal("organization.id", organization);
    if (orderBy == null) {
      orderBy = OrderBy.desc("createdAt");
    }
    return service.findAll(builder.build(), orderBy.toSort());
  }

  public Recommend recommend(Long id) {
    return service.findById(id);
  }
}
