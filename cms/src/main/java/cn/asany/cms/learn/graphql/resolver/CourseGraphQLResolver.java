/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.cms.learn.graphql.resolver;

import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.graphql.input.CommentWhereInput;
import cn.asany.cms.article.graphql.resolver.ArticleGraphQLResolver;
import cn.asany.cms.article.graphql.type.CommentConnection;
import cn.asany.cms.article.service.ArticleService;
import cn.asany.cms.learn.domain.Course;
import cn.asany.cms.learn.domain.Lesson;
import cn.asany.cms.learn.domain.enums.LearnerType;
import cn.asany.cms.learn.graphql.inputs.LearnerFilter;
import cn.asany.cms.learn.graphql.inputs.LessonRecordWhereInput;
import cn.asany.cms.learn.graphql.types.CourseConnection;
import cn.asany.cms.learn.graphql.types.LearnerConnection;
import cn.asany.cms.learn.graphql.types.LessonRecordConnection;
import cn.asany.cms.learn.service.LearnerService;
import cn.asany.cms.learn.service.LessonRecordService;
import cn.asany.cms.learn.service.LessonService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.*;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.graphql.util.Kit;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class CourseGraphQLResolver implements GraphQLResolver<Course> {

  @Autowired private LessonService lessonService;
  @Autowired private LearnerService learnerService;
  @Autowired private LessonRecordService lessonRecordService;
  @Autowired private ArticleService articleService;
  @Autowired private ArticleGraphQLResolver articleGraphQLResolver;

  public List<Lesson> lessons(Course course) {
    return this.lessonService.lessonsByCourseId(course);
  }

  public String learningProgress(Course course, Long learner) {
    return this.lessonService.getLearningProgress(course, learner);
  }

  public CourseConnection compulsoryCourseAndRecords(
      Course course, LearnerFilter filter, int page, int pageSize, Sort orderBy) {
    PropertyFilter filter1 = ObjectUtil.defaultValue(filter, new LearnerFilter()).toFilter();
    return Kit.connection(
        lessonRecordService.compulsoryCourseAndRecords(
            PageRequest.of(page - 1, pageSize, orderBy), filter1),
        CourseConnection.class);
  }

  public LearnerConnection learners(
      Course course, LearnerFilter filter, int page, int pageSize, Sort orderBy) {
    PropertyFilter propertyFilter = ObjectUtil.defaultValue(filter, new LearnerFilter()).toFilter();
    propertyFilter.equal("course", course.getId());

    Page vpage = null;
    if (filter != null && LearnerType.compulsory == filter.getType()) {
      //      filter
      //          .getEmployeeBuilder()
      //          .and(
      //              new SecurityScopeEmployeeSpecification(
      //                  course.getLearnerScope().stream()
      //                      .map(item -> SecurityScope.newInstance(item.getScope()))
      //                      .collect(Collectors.toList())));
      //      Set<Long> stringSet = new HashSet<>();
      //      course
      //          .getLearnerScope()
      //          .forEach(
      //              id -> {
      //                String str = id.getScope();
      //                List<String> strs = new ArrayList<>(Arrays.asList(str.split("_")));
      //                String type = strs.remove(0);
      //                String value = String.join("_", strs.toArray(new String[strs.size()]));
      //                stringSet.add(Long.valueOf(value));
      //              });
      //      PropertyFilterBuilder builder1 = new PropertyFilterBuilder();
      //      builder1.in("id", stringSet.toArray());
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
      vpage = learnerService.findPage(PageRequest.of(page - 1, pageSize, orderBy), propertyFilter);
    }
    return Kit.connection(vpage, LearnerConnection.class);
  }

  public LessonRecordConnection lessonRecords(
      Course course, LessonRecordWhereInput where, int page, int pageSize, Sort orderBy) {
    PropertyFilter propertyFilter =
        ObjectUtil.defaultValue(where, new LessonRecordWhereInput()).toFilter();
    return Kit.connection(
        lessonRecordService.findPage(PageRequest.of(page - 1, pageSize, orderBy), propertyFilter),
        LessonRecordConnection.class);
  }

  public CommentConnection comments(
      Course course, CommentWhereInput where, int page, int pageSize, Sort orderBy) {
    CommentConnection connection = new CommentConnection();
    List<CommentConnection.CommentEdge> comments = new ArrayList<>();
    List<Lesson> lessons = lessonService.findLessonByCourse(course.getId());
    for (Lesson lesson : lessons) {
      Article article = articleService.get(lesson.getArticle().getId());
      CommentConnection commentConnection =
          articleGraphQLResolver.comments(article, where, page, pageSize, orderBy);
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

  public Boolean hasJoined(Course course, Long employeeId) {
    return learnerService.hasJoined(course.getId(), employeeId);
  }
}
