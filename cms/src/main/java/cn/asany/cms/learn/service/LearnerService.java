package cn.asany.cms.learn.service;

import cn.asany.cms.learn.bean.Course;
import cn.asany.cms.learn.bean.Learner;
import cn.asany.cms.learn.bean.LessonRecord;
import cn.asany.cms.learn.dao.LearnerDao;
import cn.asany.cms.learn.dao.LessonRecordDao;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service("learnerService")
public class LearnerService {

  @Autowired private LearnerDao learnerDao;

  @Autowired private LessonRecordDao lessonRecordDao;

  public List<Learner> learnerByCourseId(Long courseId) {
    Course course = new Course();
    course.setId(courseId);
    return learnerDao.findByCourse(course);
  }

  public Learner createLearner(Learner learner) {
    Learner byCourseAndEmployee =
        learnerDao.findByCourseAndEmployee(learner.getCourse(), learner.getEmployee());
    if (byCourseAndEmployee != null) {
      log.debug(
          "课程{}的学习人{}已存在",
          byCourseAndEmployee.getCourse().getId(),
          byCourseAndEmployee.getEmployee());
      return byCourseAndEmployee;
    }
    return learnerDao.save(learner);
  }

  public Learner updateLearner(Learner learner) {
    return learnerDao.update(learner, true);
  }

  public Boolean removeLearner(Long id) {
    learnerDao.deleteById(id);
    return true;
  }

  public Page<Learner> findPage(Pageable pageable, List<PropertyFilter> filter) {
    if (pageable.getSort().isUnsorted()) {
      pageable =
          PageRequest.of(
              pageable.getPageNumber(), pageable.getPageSize(), OrderBy.desc("createdAt").toSort());
    }
    return learnerDao.findPage(pageable, filter);
  }

  public float lengthStudy(Course course, Long learner) {
    Learner learner1 = learnerDao.findByCourseAndEmployee(course, learner);
    if (learner1 == null) {
      return 0;
    }
    float learningProgress = (float) learner1.getLearningProgress() / 100;
    return learningProgress * course.getDuration();
  }

  public Date lastStudyTime(Learner learner, int page, int pageSize) {
    PropertyFilterBuilder builder = new PropertyFilterBuilder();
    if (learner.getEmployee() != null) {
      builder.equal("learner.employee.id", learner.getEmployee());
    }
    if (learner.getCourse() != null) {
      builder.equal("course", learner.getCourse());
    }
    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("updatedAt").descending());
    Page<LessonRecord> lessonRecordPager = lessonRecordDao.findPage(pageable, builder.build());
    if (CollectionUtils.isEmpty(lessonRecordPager.getContent())) {
      return null;
    }
    LessonRecord record = lessonRecordPager.getContent().get(0);
    return record.getUpdatedAt();
  }

  public float totalLearningTime(Long employee) {
    List<Learner> learners = learnerDao.findByEmployee(employee);
    // 累计时长
    int totalDuration = 0;
    for (Learner learner : learners) {
      Course course = learner.getCourse();
      float duration = course.getDuration();
      float learningProgress = (float) learner.getLearningProgress() / 100;
      totalDuration += duration * learningProgress;
    }
    return totalDuration;
  }

  public Learner findByCourseAndEmployee(Course course, Long employee) {
    return learnerDao.findByCourseAndEmployee(course, employee);
  }

  // 查询学习者id
  public Long findLearner(Long employee, Course course) {
    Learner learner = learnerDao.findByCourseAndEmployee(course, employee);
    if (learner != null) {
      return learner.getId();
    }
    return null;
  }

  public int findLearningProgress(Long employee, Course course) {
    Learner learner = learnerDao.findByCourseAndEmployee(course, employee);
    if (learner != null) {
      return learner.getLearningProgress();
    }
    return 0;
  }

  public Boolean hasJoined(Long courseId, Long employeeId) {
    return learnerDao.findByCourseAndEmployee(Course.builder().id(courseId).build(), employeeId)
        != null;
  }

  public List<Learner> findAllByEmployee(Long learner) {
    return learnerDao.findByEmployee(learner);
  }

  //    public List<Employee> lengthStudy(Course course, Long learner) {
  //        Employee employee = new Employee();
  //        employee.setId(learner);
  //        Learner learner1 = learnerDao.findByCourseAndEmployee(course, employee);
  //        float learningProgress = (float) learner1.getLearningProgress() / 100;
  //        return new ArrayList <>();
  //    }
}
