package cn.asany.workflow.core.service;

import java.util.List;
import net.whir.hos.issue.workflow.bean.WorkflowSchedule;
import net.whir.hos.issue.workflow.dao.WorkflowScheduleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author penghanying @ClassName: WorkflowScheduleService @Description: (这里用一句话描述这个类的作用)
 * @date 2019/5/26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowScheduleService {

  @Autowired private WorkflowScheduleDao workflowScheduleDao;

  /**
   * @ClassName: WorkflowScheduleService @Description: 记录每一次操作的数据
   *
   * @author penghanying
   * @date 2019/5/26
   */
  public WorkflowSchedule saveWorkflowSchedule(WorkflowSchedule workflowSchedule) {
    return workflowScheduleDao.save(workflowSchedule);
  }

  /**
   * @ClassName: WorkflowScheduleService @Description: 根据问题id,项目id，查询所有操作记录
   *
   * @param type 问题类型id
   * @param project 项目的id
   * @author penghanying
   * @date 2019/5/31
   */
  public List<WorkflowSchedule> issueOperateLog(Long issue, Long project) {
    return workflowScheduleDao.findAll(
        (root, query, builder) ->
            builder.and(
                builder.equal(root.get("issue"), issue),
                builder.equal(root.get("project"), project)));
  }
}
