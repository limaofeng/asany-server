package cn.asany.workflow.state.dao;

/**
 * ηΆζη DAO
 *
 * @author limaofeng
 * @date 2019/5/23
 */
import cn.asany.workflow.state.domain.State;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateDao extends JpaRepository<State, Long> {}
