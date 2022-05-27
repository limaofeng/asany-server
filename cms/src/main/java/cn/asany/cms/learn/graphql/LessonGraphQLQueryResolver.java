package cn.asany.cms.learn.graphql;

import cn.asany.cms.learn.domain.Lesson;
import cn.asany.cms.learn.service.LessonService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired private LessonService lessonService;

  public Lesson lesson(Long id) {
    return lessonService.findLessonById(id);
  }
}
