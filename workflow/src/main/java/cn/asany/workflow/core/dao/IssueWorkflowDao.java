package cn.asany.workflow.core.dao;

/**
 * @author penghanying @ClassName: IssueWorkflowDao @Description: 工作流的Dao(这里用一句话描述这个类的作用)
 * @date 2019/5/23
 */
import cn.asany.workflow.core.bean.Workflow;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueWorkflowDao extends JpaRepository<Workflow, Long> {}
