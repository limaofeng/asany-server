package cn.asany.cms.learn.graphql;

import cn.asany.cms.learn.domain.Course;
import cn.asany.cms.learn.graphql.inputs.CourseInput;
import cn.asany.cms.learn.service.CourseService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseGraphQLMutationResolver implements GraphQLMutationResolver {

  @Autowired private CourseService courseService;

  public Course createCourse(CourseInput input) {
    return courseService.addCourse(input);
  }

  public Course updateCourse(Long id, CourseInput input, Boolean merge) {
    return courseService.updateCourse(id, input, merge);
  }

  public Boolean removeCourse(Long id) {
    return courseService.deleteCourse(id);
  }
}
