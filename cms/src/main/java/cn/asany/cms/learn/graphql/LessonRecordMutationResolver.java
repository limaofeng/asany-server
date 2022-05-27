package cn.asany.cms.learn.graphql;

import cn.asany.cms.learn.domain.LessonRecord;
import cn.asany.cms.learn.domain.enums.LearnerType;
import cn.asany.cms.learn.service.LessonRecordService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonRecordMutationResolver implements GraphQLMutationResolver {

  @Autowired private LessonRecordService lessonRecordService;

  public LessonRecord updateLearningProgress(
      Long course, Long lesson, Long learner, int progress, LearnerType type) {
    return this.lessonRecordService.updateLearningProgress(course, lesson, learner, progress, type);
  }
}
