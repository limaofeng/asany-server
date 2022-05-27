package cn.asany.workflow.state.graphql.filter;

import cn.asany.workflow.state.domain.State;
import lombok.Data;
import org.jfantasy.graphql.inputs.QueryFilter;

/**
 * 状态筛选
 *
 * @author: limaofeng
 * @date: 2019/6/11 18:00
 */
@Data
public class StateFilter extends QueryFilter<StateFilter, State> {}
