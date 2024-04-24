package cn.asany.pm.issue.priority.graphql.filter;

import cn.asany.pm.issue.priority.domain.Priority;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.inputs.WhereInput;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class IssuePriorityWhereInput extends WhereInput<IssuePriorityWhereInput, Priority> {}
