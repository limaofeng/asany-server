package cn.asany.cms.learn.dao.listener;

import cn.asany.cms.learn.domain.Course;
import cn.asany.cms.learn.event.CourseCreatedEvent;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostInsertEvent;
import org.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019/11/27 11:41 上午
 */
@Component
public class CourseInsertOrUpdateListener extends AbstractChangedListener<Course> {

  public CourseInsertOrUpdateListener() {
    super(EventType.POST_COMMIT_INSERT);
  }

  @Override
  protected void onPostInsert(Course entity, PostInsertEvent event) {
    this.applicationContext.publishEvent(CourseCreatedEvent.newInstance(entity));
  }
}
