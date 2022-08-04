package cn.asany.workflow.core.graphql.issuetype;

import java.util.List;
import lombok.Data;
import cn.asany.pm.type.bean.IssueType;
import cn.asany.pm.workflow.bean.IssueWorkflow;

/**
 * @author limaofeng@msn.com @ClassName: IssueWorkflowSchemeItem @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Data
@Deprecated
public class IssueWorkflowSchemeItem {

  /** 工作流 */
  private IssueWorkflow workflow;
  /** 任务类型 */
  private List<IssueType> issueTypes;
}
