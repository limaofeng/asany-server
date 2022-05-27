package cn.asany.cms.learn.dao;

import cn.asany.cms.learn.domain.Course;
import cn.asany.cms.learn.domain.Learner;
import cn.asany.cms.learn.domain.Lesson;
import cn.asany.cms.learn.domain.LessonRecord;
import java.util.List;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRecordDao extends JpaRepository<LessonRecord, Long> {

  /**
   * 查询某用户的所有学习记录
   *
   * @param learner
   * @return
   */
  List<LessonRecord> findByLearner(Learner learner);

  /**
   * 查询某个人学习的课程下所有的学习记录
   *
   * @param lesson
   * @param learner
   * @param course
   * @return
   */
  LessonRecord findByLessonAndLearnerAndCourse(Lesson lesson, Learner learner, Course course);

  /**
   * 查询某个人对应某个课程的学习记录
   *
   * @param course
   * @param learner
   * @return
   */
  List<LessonRecord> findByCourseAndLearner(Course course, Learner learner);

  /**
   * 根据章节查询此章节下所有的学习记录
   *
   * @param lesson
   * @return
   */
  List<LessonRecord> findByLesson(Lesson lesson);

  /**
   * 根据学习人和章节查询学习记录
   *
   * @param learner
   * @param lesson
   * @return
   */
  LessonRecord findByLearnerAndLesson(Learner learner, Lesson lesson);
}
