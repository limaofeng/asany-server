package cn.asany.pm.issue.attribute.graphql.filter;

import cn.asany.pm.issue.attribute.domain.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jfantasy.graphql.inputs.QueryFilter;

/**
 * 状态过滤器
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
public class StatusFilter extends QueryFilter<StatusFilter, Status> {
  @JsonProperty("name_contains")
  private String nameContains;
}
