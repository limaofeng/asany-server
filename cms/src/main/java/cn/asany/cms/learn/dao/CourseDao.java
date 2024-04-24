package cn.asany.cms.learn.dao;

import cn.asany.cms.learn.domain.Course;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseDao extends AnyJpaRepository<Course, Long> {

  /**
   * 查询置顶课程
   *
   * @param top
   * @return
   */
  Course findByTop(boolean top);
}
