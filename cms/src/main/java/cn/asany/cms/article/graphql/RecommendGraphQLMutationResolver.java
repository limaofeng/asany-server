package cn.asany.cms.article.graphql;

import cn.asany.cms.article.bean.Recommend;
import cn.asany.cms.article.graphql.inputs.RecommendInput;
import cn.asany.cms.article.service.RecommendService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @Description @Author ChenWenJie @Data 2020/10/22 11:28 上午 */
@Component
public class RecommendGraphQLMutationResolver implements GraphQLMutationResolver {
  @Autowired private RecommendService service;

  public Recommend createRecommend(RecommendInput input) {
    return service.createRecommend(input.build());
  }

  public Recommend updateRecommend(Long id, Boolean merge, RecommendInput input) {
    return service.updateRecommend(id, merge, input.build());
  }

  public Boolean deleteRecommend(Long id) {
    service.deleteRecommend(id);
    return true;
  }
}
