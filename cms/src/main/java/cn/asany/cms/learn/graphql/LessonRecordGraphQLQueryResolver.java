package cn.asany.cms.learn.graphql;

import cn.asany.cms.learn.graphql.inputs.LessonRecordFilter;
import cn.asany.cms.learn.graphql.types.LessonRecordConnection;
import cn.asany.cms.learn.service.LessonRecordService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LessonRecordGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired private LessonRecordService lessonRecordService;

  public LessonRecordConnection lessonRecords(
      LessonRecordFilter filter, int page, int pageSize, OrderBy orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new LessonRecordFilter()).getBuilder();
    return Kit.connection(
        lessonRecordService.findPage(new Pager<>(page, pageSize, orderBy), builder.build()),
        LessonRecordConnection.class);
  }
}
