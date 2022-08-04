package cn.asany.cms.learn.event;

import cn.asany.cms.learn.domain.Course;
import org.springframework.context.ApplicationEvent;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
public class CourseCreatedEvent extends ApplicationEvent {

  CourseCreatedEvent(Long id) {
    super(id);
  }

  public String getCourseId() {
    return this.getSource().toString();
  }

  public static CourseCreatedEvent newInstance(Course article) {
    return new CourseCreatedEvent(article.getId());
  }
}
