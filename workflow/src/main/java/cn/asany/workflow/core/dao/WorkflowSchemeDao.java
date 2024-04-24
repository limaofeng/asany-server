package cn.asany.workflow.core.dao;

/*
 *  工作流方案
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
import cn.asany.workflow.core.domain.WorkflowScheme;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowSchemeDao extends AnyJpaRepository<WorkflowScheme, Long> {}
