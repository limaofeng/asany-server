package cn.asany.cms.learn.dao;

import cn.asany.cms.learn.domain.Course;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseDao extends JpaRepository<Course, Long> {

  /**
   * 查询置顶课程
   *
   * @param top
   * @return
   */
  Course findByTop(boolean top);
}
