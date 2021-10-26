package cn.asany.workflow.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.whir.hos.issue.main.bean.Issue;
import net.whir.hos.issue.security.bean.Permission;
import net.whir.hos.issue.security.dao.PermissionDao;
import net.whir.hos.issue.workflow.bean.*;
import net.whir.hos.issue.workflow.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author penghanying @ClassName: IssueWorkflowService @Description: 工作流的service(这里用一句话描述这个类的作用)
 * @date 2019/5/23
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class IssueWorkflowService {

  @Autowired private IssueWorkflowDao issueWorkflowDao;

  @Autowired private WorkflowAndIssueTypeDao workflowAndIssueTypeDao;

  @Autowired private IssueWorkflowSchemeDao issueWorkflowSchemeDao;

  @Autowired private IssueWorkflowStepDao issueWorkflowStepDao;

  @Autowired private IssueWorkflowStepTransitionDao transitionDao;

  @Autowired private IssueWorkflowStepTransitionConditionDao workflowStepTransitionConditionDao;

  @Autowired private PermissionDao permissionDao;

  /**
   * @ClassName: IssueWorkflowService @Description: 查询全部工作流
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public List<IssueWorkflow> issueWorkflows() {
    return issueWorkflowDao.findAll();
  }

  /**
   * @ClassName: IssueWorkflowService @Description: 查询一个工作流
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueWorkflow issueWorkflow(Long id) {
    return issueWorkflowDao.findById(id).orElse(null);
  }

  /**
   * @ClassName: IssueWorkflowService @Description: 创建工作流
   *
   * @author penghanying
   * @date 2019/5/24
   */
  public IssueWorkflow createIssueWorkflow(IssueWorkflow issueWorkflow) {
    return issueWorkflowDao.save(issueWorkflow);
  }

  /**
   * @ClassName: IssueWorkflowService @Description: 修改工作流
   *
   * @author penghanying
   * @date 2019/5/24
   */
  public IssueWorkflow updateIssueWorkflow(Long id, Boolean merge, IssueWorkflow issueWorkflow) {
    issueWorkflow.setId(id);
    return issueWorkflowDao.update(issueWorkflow, merge);
  }

  /**
   * @ClassName: IssueWorkflowService @Description: 删除工作流
   *
   * @author penghanying
   * @date 2019/5/24
   */
  public Boolean removeIssueWorkflow(Long id) {
    // 根据工作流id，删除工作流与任务类型的中间表
    List<WorkflowAndIssueType> all =
        workflowAndIssueTypeDao.findAll(
            Example.of(
                WorkflowAndIssueType.builder()
                    .workflow(IssueWorkflow.builder().id(id).build())
                    .build()));
    if (all.size() > 0) {
      workflowAndIssueTypeDao.deleteAll(all);
    }
    // 删除工作流中的数据
    issueWorkflowDao.deleteById(id);
    return true;
  }

  /**
   * 获取问题当前对应的操作
   *
   * @param issue
   * @return
   */
  public List<IssueWorkflowStepTransition> getWorkflowStepTransitions(Issue issue, Long user) {
    IssueWorkflowScheme workflowScheme = issue.getProject().getWorkflowScheme();

    if (workflowScheme == null) {
      workflowScheme = issueWorkflowSchemeDao.findById(Long.valueOf(1)).orElse(null);
    }

    // 获取问题工作流
    Optional<WorkflowAndIssueType> optionalWorkflowAndIssueType =
        workflowScheme.getWorkflows().stream()
            .filter(
                item ->
                    item.getIssueTypes().stream()
                        .anyMatch(type -> type.getId().equals(issue.getType().getId())))
            .findAny();
    IssueWorkflow workflow;
    if (optionalWorkflowAndIssueType.isPresent()) {
      workflow = optionalWorkflowAndIssueType.get().getWorkflow();
    } else {
      workflow = workflowScheme.getDefaultWorkflow();
    }

    // 获取对应的步骤
    Optional<IssueWorkflowStep> issueWorkflowStep =
        workflow.getSteps().stream()
            .filter(item -> item.getState().getId().equals(issue.getStatus().getId()))
            .findAny();

    if (issueWorkflowStep.isPresent()) {
      // 查询该步骤应该有的全部操作
      List<IssueWorkflowStepTransition> all =
          transitionDao.findAll(
              Example.of(
                  IssueWorkflowStepTransition.builder().step(issueWorkflowStep.get()).build()));
      return all;
    } else {
      log.error("需要匹配默认的问题方案");
      return new ArrayList<>();
    }
  }

  /**
   * @ClassName: IssueWorkflowService @Description: 通过操作的id，查询权限
   *
   * @author penghanying
   * @date 2019/6/11
   */
  public List<Permission> getGrants(Long id) {
    List<IssueWorkflowStepTransitionCondition> conditionDaoAll =
        workflowStepTransitionConditionDao.findAll(
            Example.of(IssueWorkflowStepTransitionCondition.builder().id(id).build()));
    List<Permission> permissions = new ArrayList<>();
    if (conditionDaoAll.size() > 0) {
      for (IssueWorkflowStepTransitionCondition condition : conditionDaoAll) {
        String value = condition.getValue();
        List<Permission> all =
            permissionDao.findAll(Example.of(Permission.builder().code(value).build()));
        for (Permission permission : all) {
          permissions.add(permission);
        }
      }
      return permissions;
    }
    return null;
  }
}
