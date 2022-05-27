package cn.asany.cms.learn.graphql.resolver;

import cn.asany.cms.learn.domain.Learner;
import cn.asany.cms.learn.service.LearnerService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LearnerGraphQLResolver implements GraphQLResolver<Learner> {

  @Autowired private LearnerService learnerService;

  public float lengthStudy(Learner learner) {
    return this.learnerService.lengthStudy(learner.getCourse(), learner.getEmployee());
  }

  public Date lastStudyTime(Learner learner, int page, int pageSize) {
    return this.learnerService.lastStudyTime(learner, page, pageSize);
  }
}
