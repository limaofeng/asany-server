package cn.asany.cms.learn.service;

import cn.asany.cms.learn.bean.Course;
import cn.asany.cms.learn.bean.Learner;
import cn.asany.cms.learn.bean.Lesson;
import cn.asany.cms.learn.bean.LessonRecord;
import cn.asany.cms.learn.bean.enums.LearnerType;
import cn.asany.cms.learn.dao.CourseDao;
import cn.asany.cms.learn.dao.LearnerDao;
import cn.asany.cms.learn.dao.LessonDao;
import cn.asany.cms.learn.dao.LessonRecordDao;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("lessonRecordService")
@Transactional(rollbackFor = Exception.class)
public class LessonRecordService {

  @Autowired private LessonRecordDao lessonRecordDao;

  @Autowired private LearnerDao learnerDao;

  @Autowired private CourseDao courseDao;

  @Autowired private LessonDao lessonDao;
  /** 检查章节是否学习完,最大值为100 */
  private static final Integer COMPLETED = 100;

  public LessonRecord updateLearningProgress(
      Long courseId, Long lessonId, Long learnerId, int progress, LearnerType type) {
    Course course = new Course();
    course.setId(courseId);
    Lesson lesson = new Lesson();
    lesson.setId(lessonId);
    // 判断是否是通过添加选修课添加的学习人，如果是则此时存在这个学习人但是没有学习记录，此时只需添加学习记录
    Learner learner = learnerDao.findByCourseAndEmployee(course, learnerId);
    if (learner == null) {
      Learner courseLearner = new Learner();
      courseLearner.setEmployee(learnerId);
      courseLearner.setCourse(course);
      courseLearner.setType(type);
      courseLearner.setLearningProgress(0);
      learner = learnerDao.save(courseLearner);
    }
    LessonRecord lessonRecord = new LessonRecord();
    LessonRecord record = lessonRecordDao.findByLessonAndLearnerAndCourse(lesson, learner, course);
    if (record == null) {
      // 如果之前不存在学习记录则直接添加
      lessonRecord.setCourse(course);
      lessonRecord.setLesson(lesson);
      lessonRecord.setLearner(learner);
      if (progress > COMPLETED) {
        lessonRecord.setLessonLearningProgress(COMPLETED);
      } else {
        lessonRecord.setLessonLearningProgress(progress);
      }
      lessonRecordDao.save(lessonRecord);
    } else {
      // 检查之前的学习进度是否为完成
      if (record.getLessonLearningProgress() != COMPLETED) {
        if (progress + record.getLessonLearningProgress() >= COMPLETED) {
          lessonRecord.setLessonLearningProgress(COMPLETED);
        } else {
          lessonRecord.setLessonLearningProgress(progress + record.getLessonLearningProgress());
        }
        lessonRecord.setId(record.getId());
        lessonRecord = lessonRecordDao.update(lessonRecord, true);
      }
    }
    int sum = 0;
    // 该课程总章节数
    List<Lesson> lessonList = lessonDao.findByCourse(course);
    int totalLesson = lessonList.size() * 100;
    // 该学习人总的学习记录
    List<LessonRecord> lessonRecordList = lessonRecordDao.findByCourseAndLearner(course, learner);
    for (LessonRecord lessonRecord1 : lessonRecordList) {
      // 总得学习进度
      sum += lessonRecord1.getLessonLearningProgress();
    }
    // 该课程总的学习进度
    float learningProgress = ((float) sum / (float) totalLesson) * 100;
    Learner byCourseAndEmployee = learnerDao.findByCourseAndEmployee(course, learnerId);
    Learner learner1 = new Learner();
    learner1.setId(byCourseAndEmployee.getId());
    learner1.setLearningProgress((int) learningProgress);
    learnerDao.update(learner1, true);
    return lessonRecord;
  }

  public Pager<Course> compulsoryCourseAndRecords(
      Pager<Course> pager, List<PropertyFilter> filters) {
    Set<Course> courses = new HashSet<>();
    // 查询该用户的所有课程（区分选修必修）
    List<Learner> learnerList = learnerDao.findAll(filters);
    for (Learner learner : learnerList) {
      // 必修课程
      Course course = courseDao.findById(learner.getCourse().getId()).get();
      courses.add(course);
      // 查询出该用户所有的学习记录
      List<LessonRecord> lessonRecordList = lessonRecordDao.findByLearner(learner);
      for (LessonRecord lessonRecord : lessonRecordList) {
        Course lessonRecordCourse = courseDao.findById(lessonRecord.getCourse().getId()).get();
        courses.add(lessonRecordCourse);
      }
    }
    List<Course> courseList = new ArrayList<>(courses);
    pager.reset(courseList);
    return pager;
  }

  public Pager<LessonRecord> findPage(Pager<LessonRecord> pager, List<PropertyFilter> filters) {
    if (pager.getOrderBy() == null) {
      pager.setOrderBy(OrderBy.desc("createdAt"));
    }
    return lessonRecordDao.findPager(pager, filters);
  }
}
