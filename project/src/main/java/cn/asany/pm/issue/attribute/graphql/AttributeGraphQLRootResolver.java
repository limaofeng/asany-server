package cn.asany.pm.issue.attribute.graphql;

import cn.asany.pm.issue.attribute.domain.Status;
import cn.asany.pm.issue.attribute.graphql.connection.StatusConnection;
import cn.asany.pm.issue.attribute.graphql.filter.StatusWhereInput;
import cn.asany.pm.issue.attribute.service.StatusService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 属性相关接口
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class AttributeGraphQLRootResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final StatusService statusService;

  public AttributeGraphQLRootResolver(StatusService statusService) {
    this.statusService = statusService;
  }

  /**
   * 查询任务状态
   *
   * @param where 过滤器
   * @param page 分页对象
   * @param pageSize 每页显示条数
   * @param orderBy 排序
   * @return StatusConnection
   */
  public StatusConnection issueStatuses(
      StatusWhereInput where, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        statusService.findPage(pageable, where.toFilter()), StatusConnection.class);
  }

  /** 增加任务状态 */
  protected Status createIssueStatus(Status status) {
    return statusService.save(status);
  }
}
