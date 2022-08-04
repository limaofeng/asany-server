package cn.asany.pm.issue.type.graphql;

import cn.asany.pm.issue.type.domain.IssueType;
import cn.asany.pm.issue.type.domain.IssueTypeScheme;
import cn.asany.pm.issue.type.service.IssueTypeSchemeService;
import cn.asany.pm.issue.type.service.IssueTypeService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 问题类型
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Component
public class TypeGraphQLQueryResolver implements GraphQLQueryResolver {
  /** 任务类型的service */
  @Autowired private IssueTypeService issueTypeService;

  /** 任务类型方案的service */
  @Autowired private IssueTypeSchemeService issueTypeSchemeService;

  /** 查询全部任务类型 issueTypes:[IssueType] */
  public List<IssueType> issueTypes() {
    return issueTypeService.issueTypes();
  }

  /**
   * 查询全部任务类型方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<IssueTypeScheme> issueTypeSchemes() {
    return issueTypeSchemeService.issueTypeSchemes();
  }
}
