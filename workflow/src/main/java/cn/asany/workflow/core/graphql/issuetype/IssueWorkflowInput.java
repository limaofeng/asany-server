package cn.asany.workflow.core.graphql.issuetype;

import java.util.List;
import lombok.Data;

/**
 * @author limaofeng@msn.com @ClassName: IssueWorkflowInput @Description: 创建工作流的输入(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Data
public class IssueWorkflowInput {

  // 名称
  private String name;
  // 描述
  private String description;
  // 步骤的id
  private List<Long> steps;
}
