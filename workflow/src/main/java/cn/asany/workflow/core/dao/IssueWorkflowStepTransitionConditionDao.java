package cn.asany.workflow.core.dao;

/**
 * @author penghanying @ClassName: IssuePermissionConditionDao @Description: (这里用一句话描述这个类的作用)
 * @date 2019/6/11
 */
import net.whir.hos.issue.workflow.bean.IssueWorkflowStepTransitionCondition;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueWorkflowStepTransitionConditionDao
    extends JpaRepository<IssueWorkflowStepTransitionCondition, Long> {}
