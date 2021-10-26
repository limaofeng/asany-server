package cn.asany.workflow.core.dao;

/**
 * @author penghanying @ClassName: WorkflowScheduleDao @Description: (这里用一句话描述这个类的作用)
 * @date 2019/5/26
 */
import net.whir.hos.issue.workflow.bean.WorkflowSchedule;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowScheduleDao extends JpaRepository<WorkflowSchedule, Long> {}
