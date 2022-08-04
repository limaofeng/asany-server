package cn.asany.pm.workflow.graphql.input;

import cn.asany.pm.issue.type.domain.IssueType;
import cn.asany.pm.workflow.bean.Workflow;
import java.util.List;
import lombok.Data;

/**
 * @author limaofeng@msn.com @ClassName: IssueWorkflowSchemeItem @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Data
@Deprecated
public class IssueWorkflowSchemeItem {

  /** 工作流 */
  private Workflow workflow;
  /** 任务类型 */
  private List<IssueType> issueTypes;
}
