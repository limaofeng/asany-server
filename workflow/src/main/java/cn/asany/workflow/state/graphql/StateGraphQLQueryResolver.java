package cn.asany.workflow.state.graphql;

import cn.asany.workflow.state.bean.State;
import cn.asany.workflow.state.graphql.connection.StateConnection;
import cn.asany.workflow.state.graphql.filter.StateFilter;
import cn.asany.workflow.state.service.StateService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.ArrayList;
import java.util.List;
import org.jfantasy.framework.dao.OrderBy;
import org.springframework.data.domain.Pageable;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @Author: fengmeng @Date: 2019/5/13 18:25 */
@Component
public class StateGraphQLQueryResolver implements GraphQLQueryResolver {
  @Autowired private StateService stateService;

  /** 增加任务状态 */
  /** 查询所有优先级 */
  public StateConnection states(StateFilter filter, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
    List<PropertyFilter> list = new ArrayList<>();
    return Kit.connection(stateService.findPage(pageable, list), StateConnection.class);
  }
}
