package cn.asany.cms.learn.dao;

import cn.asany.cms.learn.bean.Course;
import cn.asany.cms.learn.bean.LearnerScope;
import java.util.List;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearnerScopeDao extends JpaRepository<LearnerScope, Long> {

  List<LearnerScope> findByCourse(Course course);

  LearnerScope findByCourseAndScope(Course course, String users);
}
