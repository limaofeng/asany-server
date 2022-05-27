package cn.asany.workflow.core.dao;

/*
 * 工作流的Dao
 *
 * @author limaofeng@msn.com
 * @date 2022/5/28
 */
import cn.asany.workflow.core.domain.Workflow;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowDao extends JpaRepository<Workflow, Long> {}
