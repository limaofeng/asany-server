package cn.asany.pm.issue.core.convert;

import cn.asany.pm.issue.core.domain.Issue;
import cn.asany.pm.issue.core.graphql.input.IssueCreateInput;
import cn.asany.pm.issue.core.graphql.input.IssueUpdateInput;
import org.mapstruct.*;

/**
 * 项目 转换器
 *
 * @author limaofeng
 */
@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true),
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IssueConverter {

  /**
   * 将 IssueCreateInput 转换为 Project
   *
   * @param input 输入对象
   * @return Project
   */
  @Mappings({})
  Issue toIssue(IssueCreateInput input);

  /**
   * 将 IssueUpdateInput 转换为 Project
   *
   * @param input 输入对象
   * @return Project
   */
  @Mappings({})
  Issue toIssue(IssueUpdateInput input);
}
