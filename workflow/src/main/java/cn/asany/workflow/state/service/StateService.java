package cn.asany.workflow.state.service;

import cn.asany.workflow.state.bean.State;
import cn.asany.workflow.state.dao.StateDao;
import java.util.List;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 状态的service
 *
 * @author penghanying
 * @date 2019/5/23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StateService {

  @Autowired private StateDao stateDao;

  /** 添加状态 */
  public State createState(State issueState) {
    return stateDao.save(issueState);
  }

  /** 修改状态 */
  public State updateState(Long id, Boolean merge, State issueState) {
    issueState.setId(id);
    return stateDao.update(issueState, merge);
  }

  /** 删除状态 */
  public Boolean delete(Long id) {
    stateDao.deleteById(id);
    return true;
  }

  /** 保存数据 */
  public State save(State state) {
    return stateDao.save(state);
  }

  public Pager<State> findPager(Pager<State> pager, List<PropertyFilter> filters) {
    return stateDao.findPager(pager, filters);
  }
}
