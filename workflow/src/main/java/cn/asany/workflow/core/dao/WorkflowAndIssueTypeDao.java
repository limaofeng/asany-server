package cn.asany.workflow.core.dao;

/**
 * @author penghanying @ClassName: WorkflowAndIssueTypeDao @Description:
 *     工作流与任务类型的中间表(这里用一句话描述这个类的作用)
 * @date 2019/5/23
 */
import net.whir.hos.issue.workflow.bean.WorkflowAndIssueType;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowAndIssueTypeDao extends JpaRepository<WorkflowAndIssueType, Long> {}
