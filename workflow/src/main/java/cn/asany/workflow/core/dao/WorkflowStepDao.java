package cn.asany.workflow.core.dao;

/**
 * 流程步骤的Dao
 * @author limaofeng@msn.com
 * @date 2022/5/28
 */

import cn.asany.workflow.core.domain.WorkflowStep;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowStepDao extends JpaRepository<WorkflowStep, Long> {}
