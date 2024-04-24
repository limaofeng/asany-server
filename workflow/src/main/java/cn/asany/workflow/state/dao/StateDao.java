package cn.asany.workflow.state.dao;

/**
 * 状态的 DAO
 *
 * @author limaofeng
 * @date 2022/7/28 9:12
 */
import cn.asany.workflow.state.domain.State;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateDao extends AnyJpaRepository<State, Long> {}
