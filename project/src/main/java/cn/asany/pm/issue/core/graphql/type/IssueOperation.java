package cn.asany.pm.issue.core.graphql.type;

import cn.asany.pm.issue.core.domain.Issue;
import cn.asany.pm.screen.bean.IssueScreen;
import lombok.Builder;
import lombok.Data;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Data
@Builder
public class IssueOperation {
  private Long id;
  private String name;
  private IssueScreen screen;
  private Issue issue;
  private Long user;
}
