package cn.asany.pm.workflow.dao;

/**
 * @author limaofeng@msn.com @ClassName: IssuePermissionConditionDao @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
import cn.asany.pm.workflow.bean.WorkflowStepTransitionCondition;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueWorkflowStepTransitionConditionDao
    extends JpaRepository<WorkflowStepTransitionCondition, Long> {}
