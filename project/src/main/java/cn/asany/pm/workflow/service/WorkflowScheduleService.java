package cn.asany.pm.workflow.service;

import cn.asany.pm.workflow.bean.WorkflowSchedule;
import cn.asany.pm.workflow.dao.WorkflowScheduleDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng@msn.com @ClassName: WorkflowScheduleService @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowScheduleService {

  @Autowired private WorkflowScheduleDao workflowScheduleDao;

  /**
   * @ClassName: WorkflowScheduleService @Description: 记录每一次操作的数据
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public WorkflowSchedule saveWorkflowSchedule(WorkflowSchedule workflowSchedule) {
    return workflowScheduleDao.save(workflowSchedule);
  }

  /**
   * @ClassName: WorkflowScheduleService @Description: 根据问题id,项目id，查询所有操作记录
   *
   * @param type 问题类型id
   * @param project 项目的id
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<WorkflowSchedule> issueOperateLog(Long issue, Long project) {
    return workflowScheduleDao.findAll(
        (root, query, builder) ->
            builder.and(
                builder.equal(root.get("issue"), issue),
                builder.equal(root.get("project"), project)));
  }
}
