package cn.asany.pm.workflow.dao;

/**
 * @author limaofeng@msn.com @ClassName: IssueWorkflowSchemeDao @Description:
 *     工作流方案的Dao(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
import cn.asany.pm.workflow.bean.WorkflowScheme;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueWorkflowSchemeDao extends AnyJpaRepository<WorkflowScheme, Long> {}
