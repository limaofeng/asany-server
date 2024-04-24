package cn.asany.cms.learn.dao;

import cn.asany.cms.learn.domain.Course;
import cn.asany.cms.learn.domain.Learner;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearnerDao extends AnyJpaRepository<Learner, Long> {

  /**
   * 根据课程查询学习者
   *
   * @param course
   * @return
   */
  List<Learner> findByCourse(Course course);

  /**
   * 根据课程和用户ID查询学习人
   *
   * @param course
   * @param employee
   * @return
   */
  Learner findByCourseAndEmployee(Course course, Long employee);

  /**
   * 查询某用户所有的必修课
   *
   * @param employee
   * @param type
   * @return
   */
  List<Learner> findByEmployeeAndType(Long employee, String type);

  /**
   * 根据员工ID查询学习人
   *
   * @param employee
   * @return
   */
  List<Learner> findByEmployee(Long employee);
}
