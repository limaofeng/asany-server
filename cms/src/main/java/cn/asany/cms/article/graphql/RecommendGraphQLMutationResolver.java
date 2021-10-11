package cn.asany.cms.article.graphql;

import cn.asany.cms.article.bean.Feature;
import cn.asany.cms.article.graphql.input.RecommendInput;
import cn.asany.cms.article.service.FeatureService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @Description @Author ChenWenJie @Data 2020/10/22 11:28 上午 */
@Component
public class RecommendGraphQLMutationResolver implements GraphQLMutationResolver {
  @Autowired private FeatureService service;

  public Feature createRecommend(RecommendInput input) {
    return service.createFeature(input);
  }

  public Feature updateRecommend(Long id, Boolean merge, RecommendInput input) {
    return service.updateFeature(id, merge, input);
  }

  public Boolean deleteRecommend(Long id) {
    service.deleteFeature(id);
    return true;
  }
}
