package cn.asany.cms.learn.service;

import cn.asany.cms.article.bean.Article;
import cn.asany.cms.article.graphql.ArticleGraphQLMutationResolver;
import cn.asany.cms.article.graphql.inputs.ArticleInput;
import cn.asany.cms.learn.bean.Course;
import cn.asany.cms.learn.bean.Lesson;
import cn.asany.cms.learn.bean.LessonRecord;
import cn.asany.cms.learn.bean.enums.LessonScheduleType;
import cn.asany.cms.learn.dao.LessonDao;
import cn.asany.cms.learn.dao.LessonRecordDao;
import cn.asany.cms.learn.graphql.inputs.LessonInput;
import java.text.NumberFormat;
import java.util.List;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service("lessonService")
public class LessonService {

  @Autowired private LessonDao lessonDao;

  @Autowired private LessonRecordDao lessonRecordDao;

  @Autowired private ArticleGraphQLMutationResolver articleGraphQLMutationResolver;

  public List<Lesson> lessonsByCourseId(Course course) {
    return this.lessonDao.findByCourse(course);
  }

  public String getLearningProgress(Course course, Long learner) {
    float studyTime = 0;
    List<Lesson> lessonList = this.lessonDao.findByCourse(course);
    if (CollectionUtils.isEmpty(lessonList)) {
      return null;
    }
    PropertyFilterBuilder builder = new PropertyFilterBuilder();
    builder.equal("course", course.getId());
    builder.equal("learner.employee.id", learner);
    List<LessonRecord> lessonRecords = lessonRecordDao.findAll(builder.build());
    for (LessonRecord lessonRecord : lessonRecords) {
      // 每个章节的学习时间
      float lessonProgress = course.getDuration() / lessonList.size();
      // 每个章节的学习时间 * 每个章节的学习进度得到具体的学习时间
      studyTime += ((float) lessonRecord.getLessonLearningProgress() / 100) * lessonProgress;
      if (studyTime / course.getDuration() == 0) {
        lessonRecord.setLessonScheduleType(LessonScheduleType.unStart);
      } else if (studyTime / course.getDuration() > 0 && studyTime / course.getDuration() < 1) {
        lessonRecord.setLessonScheduleType(LessonScheduleType.processing);
      } else if (studyTime / course.getDuration() == 1) {
        lessonRecord.setLessonScheduleType(LessonScheduleType.completed);
      }
      lessonRecordDao.save(lessonRecord);
    }
    NumberFormat numberFormat = NumberFormat.getInstance();
    numberFormat.setMaximumFractionDigits(2);
    return numberFormat.format(studyTime / course.getDuration() * 100) + "%";
  }

  @Transactional(rollbackFor = Exception.class)
  public Lesson createLesson(LessonInput input) {
    ArticleInput articleInput = new ArticleInput();
    BeanUtils.copyProperties(input, articleInput);
    Article article = articleGraphQLMutationResolver.createArticle(articleInput);
    Lesson lesson = new Lesson();
    lesson.setCourse(Course.builder().id(input.getCourse()).build());
    lesson.setArticle(article);
    lesson.setLessonType(input.getLessonType());
    return lessonDao.save(lesson);
  }

  @Transactional(rollbackFor = Exception.class)
  public Boolean deleteLesson(Long id) {
    Lesson lesson = lessonDao.findById(id).get();
    List<LessonRecord> lessonRecords = lessonRecordDao.findByLesson(lesson);
    lessonRecords.forEach(
        lessonRecord -> {
          lessonRecordDao.delete(lessonRecord);
        });
    lessonDao.deleteById(id);
    articleGraphQLMutationResolver.removeArticle(lesson.getArticle().getId());
    return true;
  }

  @Transactional(rollbackFor = Exception.class)
  public Lesson updateLesson(Long id, Boolean merge, LessonInput input) {
    Lesson lessonUpdate = new Lesson();
    lessonUpdate.setLessonType(input.getLessonType());
    lessonUpdate.setId(id);
    lessonDao.update(lessonUpdate, merge);
    Lesson lesson = lessonDao.findById(id).get();
    if (lesson == null) {
      return null;
    }
    ArticleInput articleInput = new ArticleInput();
    BeanUtils.copyProperties(input, articleInput);
    articleGraphQLMutationResolver.updateArticle(lesson.getArticle().getId(), merge, articleInput);
    return lesson;
  }

  public Lesson findLessonById(Long id) {
    return lessonDao.findById(id).get();
  }

  public List<Lesson> findLessonByCourse(Long courseId) {
    Course course = Course.builder().id(courseId).build();
    return lessonDao.findByCourse(course);
  }
}
