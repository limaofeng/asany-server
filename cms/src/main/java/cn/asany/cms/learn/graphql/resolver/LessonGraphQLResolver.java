package cn.asany.cms.learn.graphql.resolver;

import cn.asany.cms.article.converter.ArticleCategoryConverter;
import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.service.ArticleService;
import cn.asany.cms.learn.dao.LearnerDao;
import cn.asany.cms.learn.dao.LessonRecordDao;
import cn.asany.cms.learn.domain.Course;
import cn.asany.cms.learn.domain.Learner;
import cn.asany.cms.learn.domain.Lesson;
import cn.asany.cms.learn.domain.LessonRecord;
import cn.asany.cms.learn.service.CourseService;
import cn.asany.cms.learn.service.LessonService;
import cn.asany.storage.api.FileObject;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonGraphQLResolver implements GraphQLResolver<Lesson> {

  @Autowired private CourseService courseService;

  @Autowired private ArticleService articleService;

  @Autowired private LessonService lessonService;

  @Autowired private ArticleCategoryConverter articleCategoryConverter;
  @Autowired private LessonRecordDao lessonRecordDao;
  @Autowired private LearnerDao learnerDao;

  public Course course(Lesson lesson) {
    return this.courseService.findById(lesson.getCourse().getId());
  }

  public Article article(Lesson lesson) {
    return this.articleService.get(lesson.getArticle().getId());
  }

  public String content(Lesson lesson) {
    Article article = articleService.get(lesson.getArticle().getId());
    //    if (article.getContent() != null) {
    //      return article.getContent();
    //    }
    return null;
  }

  public String title(Lesson lesson) {
    return articleService.get(lesson.getArticle().getId()).getTitle();
  }

  //  public ArticleCategory category(Lesson lesson) {
  //    return articleService.get(lesson.getArticle().getId()).getCategory();
  //  }

  public List<ArticleCategory> channels(Lesson lesson) {
    return Collections.singletonList(lesson.getArticle().getCategory());
  }

  public List<FileObject> attachments(Lesson lesson) {
    return articleService.get(lesson.getArticle().getId()).getAttachments();
  }

  public Float lessonDuration(Lesson lesson) {
    Course course = courseService.findById(lesson.getCourse().getId());
    Float lessonDuration = null;
    if (course != null) {
      float duration = course.getDuration();
      List<Lesson> lessonList = lessonService.findLessonByCourse(course.getId());
      lessonDuration = duration / lessonList.size();
    }
    return lessonDuration;
  }

  public Float lessonProgress(Lesson lesson, Long employee) {
    Learner learner = learnerDao.findByCourseAndEmployee(lesson.getCourse(), employee);
    if (learner != null) {
      LessonRecord lessonRecord =
          lessonRecordDao.findByLearnerAndLesson(
              learner, Lesson.builder().id(lesson.getId()).build());
      if (lessonRecord != null) {
        return (float) lessonRecord.getLessonLearningProgress();
      }
    }
    return null;
  }
}
