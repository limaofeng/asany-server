package cn.asany.cms.learn.graphql;

import cn.asany.cms.learn.graphql.inputs.LessonRecordWhereInput;
import cn.asany.cms.learn.graphql.types.LessonRecordConnection;
import cn.asany.cms.learn.service.LessonRecordService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class LessonRecordGraphQLQueryResolver implements GraphQLQueryResolver {

  private final LessonRecordService lessonRecordService;

  public LessonRecordGraphQLQueryResolver(LessonRecordService lessonRecordService) {
    this.lessonRecordService = lessonRecordService;
  }

  public LessonRecordConnection lessonRecords(
      LessonRecordWhereInput where, int page, int pageSize, Sort orderBy) {
    PropertyFilter filter = ObjectUtil.defaultValue(where, new LessonRecordWhereInput()).toFilter();
    return Kit.connection(
        lessonRecordService.findPage(PageRequest.of(page - 1, pageSize, orderBy), filter),
        LessonRecordConnection.class);
  }
}
