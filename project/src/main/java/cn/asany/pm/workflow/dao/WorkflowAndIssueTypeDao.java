package cn.asany.pm.workflow.dao;

/**
 * @author limaofeng@msn.com @ClassName: WorkflowAndIssueTypeDao @Description:
 *     工作流与任务类型的中间表(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
import cn.asany.pm.workflow.bean.WorkflowAndIssueType;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowAndIssueTypeDao extends JpaRepository<WorkflowAndIssueType, Long> {}
