package cn.asany.cms.learn.service;

import cn.asany.cms.article.bean.Article;
import cn.asany.cms.article.service.ArticleService;
import cn.asany.cms.learn.bean.*;
import cn.asany.cms.learn.dao.CourseDao;
import cn.asany.cms.learn.dao.LearnerScopeDao;
import cn.asany.cms.learn.dao.LessonRecordDao;
import cn.asany.cms.learn.graphql.inputs.CourseInput;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("courseService")
public class CourseService {

  @Autowired private CourseDao courseDao;

  @Autowired private LearnerService learnerService;

  @Autowired private LessonService lessonService;

  @Autowired private ArticleService articleService;

  @Autowired private LearnerScopeDao learnerScopeDao;
  @Autowired private LessonRecordDao lessonRecordDao;

  public Course findById(Long id) {
    return courseDao.findById(id).orElse(null);
  }

  public List<Course> course(List<PropertyFilter> filters) {
    return courseDao.findAll(filters);
  }

  @Transactional(rollbackFor = Exception.class)
  public Course addCourse(CourseInput courseInput) {
    Course course = new Course();
    //        Employee employee = Employee.builder().id(courseInput.getPublishUser()).build();
    //        if (courseInput.getCover() != null) {
    //            FileObject fileObject = this.getFile(courseInput.getCover().getId());
    //            course.setCover(fileObject);
    //        }
    //        if (courseInput.getTop()) {
    //            Course courseTop = courseDao.findByTop(courseInput.getTop());
    //            if (courseTop != null) {
    //                courseTop.setTop(false);
    //                courseDao.update(courseTop, true);
    //            }
    //        }
    BeanUtils.copyProperties(courseInput, course);
    String format = DateUtil.format("yyyy-MM-dd");
    course.setPublishDate(format);
    course.setPublishUser(0L);
    Course save = courseDao.save(course);
    if (CollectionUtils.isNotEmpty(courseInput.getLearnerScope())) {
      List<String> learnerScopes = courseInput.getLearnerScope();
      for (String scope : learnerScopes) {
        LearnerScope learnerScope = new LearnerScope();
        learnerScope.setScope(scope);
        learnerScope.setCourse(save);
        learnerScopeDao.save(learnerScope);
      }
    }
    return save;
  }

  public Pager<Course> findPage(Pager<Course> pager, List<PropertyFilter> filters) {
    return courseDao.findPager(pager, filters);
  }

  @Transactional(rollbackFor = Exception.class)
  public Course updateCourse(Long id, CourseInput courseInput, Boolean merge) {
    Course course = new Course();
    if (courseInput.getTop()) {
      // 修改之前置顶的记录状态
      Course courseTop = courseDao.findByTop(courseInput.getTop());
      if (courseTop != null) {
        courseTop.setTop(false);
        courseDao.update(courseTop, merge);
      }
      course.setTop(courseInput.getTop());
    }
    course.setId(id);
    BeanUtils.copyProperties(courseInput, course);
    List<LearnerTrans> learnerTransList = new ArrayList<>();
    // 该课程新的学习人
    List<String> learnerScopesNew = courseInput.getLearnerScope();
    if (CollectionUtils.isNotEmpty(learnerScopesNew)) {
      for (String learnerScopeNew : learnerScopesNew) {
        LearnerScope learnerScope = new LearnerScope();
        learnerScope.setCourse(course);
        learnerScope.setScope(learnerScopeNew);
        LearnerScope scope = learnerScopeDao.findByCourseAndScope(course, learnerScopeNew);
        if (scope == null) {
          scope = learnerScopeDao.save(learnerScope);
        }
        LearnerTrans learnerTrans = new LearnerTrans(scope.getCourse().getId(), scope.getScope());
        learnerTransList.add(learnerTrans);
      }
      // 该课程老的学习人
      List<LearnerScope> learnerScopesOld = learnerScopeDao.findByCourse(course);
      // 如果新的学习人不包含之前老的学习人则删除老的学习人
      for (LearnerScope learnerScopeOld : learnerScopesOld) {
        LearnerTrans learnerTrans =
            new LearnerTrans(learnerScopeOld.getCourse().getId(), learnerScopeOld.getScope());
        if (!learnerTransList.contains(learnerTrans)) {
          learnerScopeDao.delete(learnerScopeOld);
          String employeeId = StringUtils.substringAfterLast(learnerScopeOld.getScope(), "_");
          Learner learner =
              learnerService.findByCourseAndEmployee(
                  Course.builder().id(id).build(), Long.valueOf(employeeId));
          if (learner != null) {
            List<LessonRecord> lessonRecords =
                lessonRecordDao.findByCourseAndLearner(Course.builder().id(id).build(), learner);
            lessonRecords.forEach(lessonRecord -> lessonRecordDao.delete(lessonRecord));
            learnerService.removeLearner(learner.getId());
          }
        }
      }
    }
    return courseDao.update(course, merge);
  }

  @Transactional(rollbackFor = Exception.class)
  public Boolean deleteCourse(Long id) {
    Course course = Course.builder().id(id).build();
    List<Lesson> lessonList = lessonService.lessonsByCourseId(course);
    for (Lesson lesson : lessonList) {
      List<LessonRecord> lessonRecords = lessonRecordDao.findByLesson(lesson);
      lessonRecords.forEach(lessonRecord -> lessonRecordDao.delete(lessonRecord));
      // 删除该课程所有章节内容
      lessonService.deleteLesson(lesson.getId());
      Article article = articleService.get(lesson.getArticle().getId());
      if (article != null) {
        articleService.deleteArticle(lesson.getArticle().getId());
      }
    }
    List<Learner> learners = learnerService.learnerByCourseId(id);
    learners.forEach(learner -> learnerService.removeLearner(learner.getId()));
    List<LearnerScope> learnerScopes =
        learnerScopeDao.findByCourse(Course.builder().id(id).build());
    learnerScopes.forEach(learnerScope -> learnerScopeDao.delete(learnerScope));
    courseDao.deleteById(id);
    return true;
  }
}
