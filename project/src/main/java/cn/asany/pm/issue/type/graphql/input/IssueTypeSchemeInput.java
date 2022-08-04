package cn.asany.pm.issue.type.graphql.input;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
import cn.asany.pm.issue.type.domain.IssueTypeScheme;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务类型方案的输入
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class IssueTypeSchemeInput extends IssueTypeScheme {
  /** 名称 */
  private String name;
  /** 描述 */
  private String description;
  /** 默认任务类型 */
  private Long defaultIssueType;
  /** 任务类型的id */
  private List<Long> issueTypes;
}
