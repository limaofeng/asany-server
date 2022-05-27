package cn.asany.cms.learn.graphql;

import cn.asany.cms.learn.domain.Lesson;
import cn.asany.cms.learn.graphql.inputs.LessonInput;
import cn.asany.cms.learn.service.LessonService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonMutationResolver implements GraphQLMutationResolver {

  @Autowired private LessonService lessonService;

  public Lesson createLesson(LessonInput input) {
    return lessonService.createLesson(input);
  }

  public boolean removeLesson(Long id) {
    return lessonService.deleteLesson(id);
  }

  public Lesson updateLesson(Long id, Boolean merge, LessonInput input) {
    return lessonService.updateLesson(id, merge, input);
  }
}
