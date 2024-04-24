package cn.asany.pm.workflow.dao;

/**
 * @author limaofeng@msn.com @ClassName: IssueWorkflowStepTransitionDao @Description:
 *     进入下一步进行的操作(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
import cn.asany.pm.workflow.bean.WorkflowStepTransition;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueWorkflowStepTransitionDao
    extends AnyJpaRepository<WorkflowStepTransition, Long> {}
