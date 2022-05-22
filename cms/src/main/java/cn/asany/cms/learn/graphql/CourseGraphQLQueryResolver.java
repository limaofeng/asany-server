package cn.asany.cms.learn.graphql;

import cn.asany.cms.learn.bean.Course;
import cn.asany.cms.learn.graphql.inputs.CourseFilter;
import cn.asany.cms.learn.graphql.types.CourseConnection;
import cn.asany.cms.learn.service.CourseService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class CourseGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired private CourseService courseService;

  public Course course(Long id) {
    return courseService.findById(id);
  }

  public CourseConnection courses(CourseFilter filter, int page, int pageSize) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new CourseFilter()).getBuilder();
    //        if (filter != null && ObjectUtils.isNotEmpty(filter.getLearnersLearner())){
    //            builder.in("learnerScope.scope", emp.getAuthoritys());
    //        }
    return Kit.connection(
        courseService.findPage(PageRequest.of(page - 1, pageSize, Sort.by("top")), builder.build()),
        CourseConnection.class);
  }
}
