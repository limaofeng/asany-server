package cn.asany.pm.issue.core.graphql.input;

import cn.asany.pm.issue.core.domain.Issue;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.graphql.inputs.QueryFilter;

/**
 * 任务筛选
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
public class IssueFilter extends QueryFilter<IssueFilter, Issue> {

  @JsonProperty(value = "priority_in")
  public void setPriority_in(Long[] priority_in) {
    builder.in("priority.id", priority_in);
  }

  @JsonProperty(value = "status_in")
  public void setStatus_in(Long[] status_in) {
    if (StringUtil.isNotBlank(status_in) && status_in.length > 0) {
      builder.in("status.id", status_in);
    }
  }

  @JsonProperty(value = "project")
  public void setProject(Long project) {
    builder.equal("project.id", project);
  }

  @JsonProperty(value = "name_contains")
  public void setNameContains(String name) {
    builder.contains("summary", name);
  }

  @JsonProperty(value = "reporter")
  public void setReporter(Long reporter) {
    builder.equal("reporter.id", reporter);
  }

  @JsonProperty(value = "type")
  public void setType(String type) {
    builder.equal("type", type);
  }
}
