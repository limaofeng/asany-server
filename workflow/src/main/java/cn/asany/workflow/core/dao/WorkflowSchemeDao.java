package cn.asany.workflow.core.dao;

/*
 *  工作流方案
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
import cn.asany.workflow.core.domain.WorkflowScheme;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowSchemeDao extends JpaRepository<WorkflowScheme, Long> {}
