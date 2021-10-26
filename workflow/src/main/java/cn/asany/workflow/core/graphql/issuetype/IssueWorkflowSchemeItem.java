package cn.asany.workflow.core.graphql.issuetype;

import java.util.List;
import lombok.Data;
import net.whir.hos.issue.type.bean.IssueType;
import net.whir.hos.issue.workflow.bean.IssueWorkflow;

/**
 * @author penghanying @ClassName: IssueWorkflowSchemeItem @Description: (这里用一句话描述这个类的作用)
 * @date 2019/5/23
 */
@Data
@Deprecated
public class IssueWorkflowSchemeItem {

  /** 工作流 */
  private IssueWorkflow workflow;
  /** 任务类型 */
  private List<IssueType> issueTypes;
}
