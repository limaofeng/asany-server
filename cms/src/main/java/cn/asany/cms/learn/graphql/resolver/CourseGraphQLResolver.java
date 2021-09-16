package cn.asany.cms.learn.graphql.resolver;

import cn.asany.cms.article.bean.Article;
import cn.asany.cms.article.graphql.input.CommentFilter;
import cn.asany.cms.article.graphql.resolver.ArticleGraphQLResolver;
import cn.asany.cms.article.graphql.type.CommentConnection;
import cn.asany.cms.article.service.ArticleService;
import cn.asany.cms.learn.bean.Course;
import cn.asany.cms.learn.bean.LearnerScope;
import cn.asany.cms.learn.bean.Lesson;
import cn.asany.cms.learn.bean.enums.LearnerType;
import cn.asany.cms.learn.graphql.inputs.LearnerFilter;
import cn.asany.cms.learn.graphql.inputs.LearnerScopeFilter;
import cn.asany.cms.learn.graphql.inputs.LessonRecordFilter;
import cn.asany.cms.learn.graphql.types.CourseConnection;
import cn.asany.cms.learn.graphql.types.LearnerConnection;
import cn.asany.cms.learn.graphql.types.LearnerScopeConnection;
import cn.asany.cms.learn.graphql.types.LessonRecordConnection;
import cn.asany.cms.learn.service.LearnerScopeService;
import cn.asany.cms.learn.service.LearnerService;
import cn.asany.cms.learn.service.LessonRecordService;
import cn.asany.cms.learn.service.LessonService;
import cn.asany.cms.permission.service.SecurityScope;
import cn.asany.cms.permission.specification.SecurityScopeEmployeeSpecification;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseGraphQLResolver implements GraphQLResolver<Course> {

  @Autowired private LessonService lessonService;
  @Autowired private LearnerService learnerService;
  @Autowired private LessonRecordService lessonRecordService;
  @Autowired private LearnerScopeService learnerScopeService;
  @Autowired private ArticleService articleService;
  @Autowired private ArticleGraphQLResolver articleGraphQLResolver;

  public List<Lesson> lessons(Course course) {
    return this.lessonService.lessonsByCourseId(course);
  }

  public String learningProgress(Course course, Long learner) {
    return this.lessonService.getLearningProgress(course, learner);
  }

  public CourseConnection compulsoryCourseAndRecords(
      Course course, LearnerFilter filter, int page, int pageSize, OrderBy orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new LearnerFilter()).getBuilder();
    return Kit.connection(
        lessonRecordService.compulsoryCourseAndRecords(
            new Pager<>(page, pageSize, orderBy), builder.build()),
        CourseConnection.class);
  }

  public LearnerConnection learners(
      Course course, LearnerFilter filter, int page, int pageSize, OrderBy orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new LearnerFilter()).getBuilder();
    builder.equal("course", course.getId());

    Pager vpager = null;
    if (filter != null && LearnerType.compulsory == filter.getType()) {
      filter
          .getEmployeeBuilder()
          .and(
              new SecurityScopeEmployeeSpecification(
                  course.getLearnerScope().stream()
                      .map(item -> SecurityScope.newInstance(item.getScope()))
                      .collect(Collectors.toList())));
      Set<Long> stringSet = new HashSet<>();
      course
          .getLearnerScope()
          .forEach(
              id -> {
                String str = id.getScope();
                List<String> strs = new ArrayList<>(Arrays.asList(str.split("_")));
                String type = strs.remove(0);
                String value = String.join("_", strs.toArray(new String[strs.size()]));
                stringSet.add(Long.valueOf(value));
              });
      PropertyFilterBuilder builder1 = new PropertyFilterBuilder();
      builder1.in("id", stringSet.toArray());
      // todo
      //            Pager<Employee> pager = employeeService.findPager(new Pager<>(page, pageSize,
      // orderBy), builder1.build());
      //            vpager = new Pager<Learner>(pager, pager.getPageItems().stream().map(item ->
      // Learner.builder()
      //                    .id(learnerService.findLearner(item, course) == null ? Long.valueOf("-"
      // + item.getId()) : learnerService.findLearner(item, course))
      //                    .employee(item)
      //                    .type(filter.getType())
      //                    .learningProgress(learnerService.findLearningProgress(item, course))
      //                    .build()).collect(Collectors.toList()));
    } else {
      vpager = learnerService.findPager(new Pager<>(page, pageSize, orderBy), builder.build());
    }
    return Kit.connection(vpager, LearnerConnection.class);
  }

  public LessonRecordConnection lessonRecords(
      Course course, LessonRecordFilter filter, int page, int pageSize, OrderBy orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new LessonRecordFilter()).getBuilder();
    return Kit.connection(
        lessonRecordService.findPage(new Pager<>(page, pageSize, orderBy), builder.build()),
        LessonRecordConnection.class);
  }

  public List<LearnerScope> learnerScopes(Course course) {
    return learnerScopeService.findLearnerScopeByCourseId(course);
  }

  public CommentConnection comments(
      Course course, CommentFilter filter, int page, int pageSize, OrderBy orderBy) {
    CommentConnection connection = new CommentConnection();
    List<CommentConnection.CommentEdge> comments = new ArrayList<>();
    List<Lesson> lessons = lessonService.findLessonByCourse(course.getId());
    for (Lesson lesson : lessons) {
      Article article = articleService.get(lesson.getArticle().getId());
      CommentConnection commentConnection =
          articleGraphQLResolver.comments(article, filter, page, pageSize, orderBy);
      if (CollectionUtils.isEmpty(commentConnection.getEdges())) {
        continue;
      }
      List<CommentConnection.CommentEdge> edges = commentConnection.getEdges();
      for (CommentConnection.CommentEdge edge : edges) {
        comments.add(edge);
      }
    }
    connection.setEdges(comments);
    return connection;
  }

  public float totalLearningTime(Course course, Long employee) {
    return learnerService.totalLearningTime(employee);
  }

  public LearnerScopeConnection scopes(
      Course course, LearnerScopeFilter filter, int page, int pageSize, OrderBy orderBy) {
    Pager<LearnerScope> pager = new Pager<>(page, pageSize, orderBy);
    return Kit.connection(
        learnerScopeService.pager(pager, filter.build()), LearnerScopeConnection.class);
  }

  public Boolean hasJoined(Course course, Long employeeId) {
    return learnerService.hasJoined(course.getId(), employeeId);
  }
}
