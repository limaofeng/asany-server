package cn.asany.workflow.core.dao;

/**
 * @author penghanying @ClassName: IssueWorkflowSchemeDao @Description: 工作流方案的Dao(这里用一句话描述这个类的作用)
 * @date 2019/5/23
 */
import net.whir.hos.issue.workflow.bean.IssueWorkflowScheme;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueWorkflowSchemeDao extends JpaRepository<IssueWorkflowScheme, Long> {}
