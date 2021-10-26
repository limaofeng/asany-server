package cn.asany.workflow.core.dao;

/**
 * @author penghanying @ClassName: IssueWorkflowStepTransitionDao @Description:
 *     进入下一步进行的操作(这里用一句话描述这个类的作用)
 * @date 2019/5/23
 */
import net.whir.hos.issue.workflow.bean.IssueWorkflowStepTransition;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueWorkflowStepTransitionDao
    extends JpaRepository<IssueWorkflowStepTransition, Long> {}
