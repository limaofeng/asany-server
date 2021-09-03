package cn.asany.cms.learn.graphql;

import cn.asany.cms.learn.bean.Learner;
import cn.asany.cms.learn.service.LearnerService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LearnerGraphQLMutationResolver implements GraphQLMutationResolver {

  @Autowired private LearnerService learnerService;

  public Learner createLearner(Learner learner) {
    return learnerService.createLearner(learner);
  }
}
