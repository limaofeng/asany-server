package cn.asany.pm.workflow.dao;

/**
 * @author limaofeng@msn.com @ClassName: IssueWorkflowStepTransitionDao @Description:
 *     进入下一步进行的操作(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
import cn.asany.pm.workflow.bean.WorkflowStepTransition;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueWorkflowStepTransitionDao
    extends JpaRepository<WorkflowStepTransition, Long> {}
