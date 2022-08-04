package cn.asany.workflow.state.dao;

/**
 * 状态的 DAO
 *
 * @author limaofeng
 * @date 2022/7/28 9:12
 */
import cn.asany.workflow.state.domain.State;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateDao extends JpaRepository<State, Long> {}
