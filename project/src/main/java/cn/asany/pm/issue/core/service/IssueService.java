/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.pm.issue.core.service;

import cn.asany.pm.field.graphql.model.IssueFieldValueInput;
import cn.asany.pm.issue.attribute.domain.Resolution;
import cn.asany.pm.issue.core.dao.IssueDao;
import cn.asany.pm.issue.core.domain.Issue;
import cn.asany.pm.issue.core.domain.TimeTrack;
import cn.asany.pm.issue.core.domain.Worklog;
import cn.asany.pm.issue.core.graphql.type.IssueOperation;
import cn.asany.pm.project.domain.Project;
import cn.asany.pm.project.domain.ProjectMember;
import cn.asany.pm.project.service.ProjectService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.spring.mvc.error.NotFoundException;
import net.asany.jfantasy.framework.util.common.ClassUtil;
import net.asany.jfantasy.framework.util.ognl.OgnlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Service
public class IssueService {
  @Autowired private IssueDao issueDao;
  //  @Autowired private WorkflowStepTransitionService workflowStepTransitionService;
  @Autowired private ProjectService projectService;
  //  @Autowired private FieldConfigurationService fieldConfigurationService;
  @Autowired private WorklogService worklogService;
  @Autowired private TimeTrackService timeTrackService;

  //  @Autowired private PermissionService permissionService;
  //    @Autowired
  //    private DepartmentService departmentService;
  //    @Autowired
  //    private EmployeeService employeeService;
  //    @Autowired
  //    private EmployeePositionDao employeePositionDao;
  //  @Autowired private IssueWorkflowStepTransitionConditionDao conditionDao;
  //  @Autowired private PermissionDao permissionsDao;
  //  @Autowired private GrantPermissionDao grantPermissionDao;
  //    @Autowired
  //    private NotificationService notificationService;

  //  @Autowired private IssueAllocationRuleService issueAllocationRuleService;

  //    @Autowired
  //    private IssueLinkProjectService issueLinkProjectService;

  //    @Autowired
  //    private TeLogDao telLogDTO;

  public Issue findById(Long id) {
    return issueDao.findById(id).orElse(null);
  }

  public Page<Issue> findPage(Pageable pageable, PropertyFilter filter) {
    return issueDao.findPage(pageable, filter);
  }

  public List<Issue> findAll(PropertyFilter filter, int size, Sort orderBy) {
    return issueDao.findAll(filter, size, orderBy);
  }

  /**
   * 保存任务
   *
   * @param issue 任务
   * @return Issue
   */
  @Transactional
  public Issue save(Issue issue) {
    Project project = issue.getProject();
    // 判断该项目
    //
    // issue.setIssueLinkProject(issueLinkProjectService.getLinkProject(issue.getProject().getId()));
    issue.setProject(project);
    //    issue.setStatus(
    //        workflowStepTransitionService.issueInitializateStatus(
    //            project.getWorkflowScheme().getId(), issue.getType().getId()));
    issue.setStartTime(new Date());
    /** 创建时间跟踪器 */
    TimeTrack timeTrack = timeTrackService.save(TimeTrack.builder().logged(0L).build());
    issue.setTimeTrack(timeTrack);
    Long assignee = 0L; // issueAllocationRuleService.getIssueAssignee(issue);
    Issue save = issueDao.save(issue);
    if (assignee != null) {
      List<IssueFieldValueInput> issueFieldValueInputs = new ArrayList<>();
      issueFieldValueInputs.add(
          IssueFieldValueInput.builder().name("assignee").value(String.valueOf(assignee)).build());
      save = issueAction(issue.getId(), 1L, issueFieldValueInputs);
      /** 发送消息 */
      //            notificationService.createNotification("1", 1L,
      // Message.builder().title("新单").content("您有新工单提醒，请及时处理").format(MessageFormat.text).build(),
      // SecurityScope.newInstance("employee" + "_" + save.getAssignee()));
    }
    // 关联电话记录表
    //        if(save != null&& telLogId!=null){
    //            TelLog telLog = telLogDTO.getOne(telLogId);
    //            if(telLog != null){
    //                telLog.setIssueId(issue.getId());
    //                //修改电话记录里的工单id
    //                telLogDTO.update(telLog,true);
    //            }
    //        }
    return save;
  }

  /**
   * 更新任务
   *
   * @param id 任务ID
   * @param issue 任务详情
   * @param merge 合并模式
   * @return Issue
   */
  @Transactional
  public Issue update(Long id, Issue issue, Boolean merge) {
    issue.setId(id);
    return issueDao.update(issue, merge);
  }

  @Transactional
  public Issue issueAction(Long issueId, Long action, List<IssueFieldValueInput> values) {
    Issue issue = issueDao.findById(issueId).orElse(null);
    Project project = projectService.get(issue.getProject().getId()).orElse(null);
    //    Status issueStatus = "";
    //        workflowStepTransitionService.issueWorkflowStatus(
    //            project.getWorkflowScheme().getId(), issue.getType().getId(), action);
    //    issue.setStatus(issueStatus);
    // 存储预设字段
    if (values != null) {
      OgnlUtil ognlUtil = OgnlUtil.getInstance();
      values.forEach(
          value -> {
            if (ClassUtil.getProperty(Issue.class, value.getName()) != null) {
              ognlUtil.setValue(value.getName(), issue, value.getValue());
            }
          });
    }
    // 存储动态字段
    //    fieldConfigurationService.issueProject(issue, action, values);
    return issueDao.save(issue);
  }

  /** 新增任务结果 */
  public Boolean resolutionIssue(Long issueId, Long resolutionId) {
    Issue issue = issueDao.findById(issueId).orElse(null);
    issue.setResolution(Resolution.builder().id(resolutionId).build());
    issueDao.save(issue);
    return true;
  }

  /** 开始任务 */
  public Boolean startIssue(Long issueId) {
    Issue issue = issueDao.findById(issueId).orElse(null);
    TimeTrack timeTrack = issue.getTimeTrack();
    timeTrack.setTrackDate(new Date());
    // 更新时间跟踪器
    timeTrackService.save(timeTrack);
    return true;
  }

  /** 暂停任务 */
  public Boolean pauseIssue(Long issueId, String content) {
    Optional<Issue> issueOptional = issueDao.findById(issueId);

    Issue issue = issueOptional.orElseThrow(() -> new NotFoundException("问题[" + issueId + "]不存在"));

    TimeTrack timeTrack = issue.getTimeTrack();
    // 记录时长分钟
    timeTrack.setLogged(
        timeTrack.getLogged()
            + (System.currentTimeMillis() - timeTrack.getTrackDate().getTime()) / (60 * 1000));
    timeTrack.setTrackDate(null);
    // 更新时间跟踪器
    timeTrackService.save(timeTrack);
    // 保存任务日志
    worklogService.save(issueId, Worklog.builder().content(content).logTime(new Date()).build());
    return true;
  }

  /** 可分配用户 */
  public List<ProjectMember> assignableUsers(Long issueId) {
    //        Issue issue = issueDao.findById(issueId).orElse(null);
    //        List<GrantPermission> grantPermissions =
    // permissionService.getGrantPermissions(issue.getProject().getPermissionScheme().getId(),
    // "ASSIGNABLE_USER");
    //        List<Employee> list = new ArrayList<>();
    //        if (grantPermissions != null) {
    //            grantPermissions.forEach(item -> {
    //                        if (SecurityType.department.equals(item.getSecurityType())) {
    //
    // departmentService.get(Long.valueOf(item.getValue())).getEmployees().forEach(
    //                                    value -> list.add(value.getEmployee())
    //                            );
    //                        } else {
    //                            list.add(employeeService.get(Long.valueOf(item.getValue())));
    //                        }
    //                    }
    //            );
    //        }
    //        return list.stream().distinct().collect(Collectors.toList());
    return new ArrayList<>();
  }

  /**
   * 该步骤是否可用
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean getGrants(IssueOperation operation) {
    // 获取问题对象
    Issue issue = operation.getIssue();
    // 获取登录用户id
    Long user = operation.getUser();
    if (user != null) {
      // 根据用户id，查询该用户所在部门
      //            List<EmployeePosition> employeePositions =
      // employeePositionDao.findAll(Example.of(EmployeePosition.builder().employee(Employee.builder().id(user).build()).build()));
      //            List<Department> departments = new ArrayList<>();
      //            if (employeePositions.size() > 0) {
      //                for (EmployeePosition employeePosition : employeePositions) {
      //                    Department department = employeePosition.getDepartment();
      //                    departments.add(department);
      //                }
      //            }
      //
      //            //根据操作id，查询该操作对应的权限
      //            List<WorkflowStepTransitionCondition> conditionDaoAll =
      // conditionDao.findAll(Example.of(WorkflowStepTransitionCondition.builder().transition(WorkflowStepTransition.builder().id(operation.getId()).build()).build()));
      //            for (WorkflowStepTransitionCondition condition : conditionDaoAll) {
      //                //根据权限编码，查询权限
      //                List<Permission> permissions =
      // permissionsDao.findAll(Example.of(Permission.builder().code(condition.getValue()).build()));
      //                for (Permission permission : permissions) {
      //                    //获取权限方案
      //                    PermissionScheme permissionScheme =
      // issue.getProject().getPermissionScheme();
      //                    //匹配用户权限
      //                    List<GrantPermission> grantPermissions =
      // grantPermissionDao.findAll(Example.of(GrantPermission.builder().securityType(SecurityType.user).scheme(permissionScheme).permission(permission).value(String.valueOf(user)).build()));
      //                    if (grantPermissions.size() > 0) {
      //                        for (GrantPermission grantPermission : grantPermissions) {
      //                            Permission permission1 = grantPermission.getPermission();
      //                            if (permission.equals(permission1)) {
      //                                return true;
      //                            } else {
      //                                return false;
      //                            }
      //                        }
      //                    }
      //                    //匹配组权限
      //                    for (Department department : departments) {
      //                        List<GrantPermission> permissionList1 =
      // grantPermissionDao.findAll(Example.of(GrantPermission.builder().securityType(SecurityType.department).scheme(permissionScheme).permission(permission).value(String.valueOf(department.getId())).build()));
      //                        if (permissionList1.size() > 0) {
      //                            for (GrantPermission grantPermission : permissionList1) {
      //                                Permission permission1 = grantPermission.getPermission();
      //                                if (permission.equals(permission1)) {
      //                                    return true;
      //                                } else {
      //                                    return false;
      //                                }
      //                            }
      //                        }
      //                    }
      //                }
      //            }
    }
    return false;
  }

  /** 获取人员列表中工单最少数的人 */
  public Long assingeeMinIssue() {
    //        if (employees.size() == 1) {
    //            return employees.get(0).getId();
    //        }
    //        List<Map<String, Object>> list = issueDao.assigneeCount();
    //        HashMap<Long, Integer> assMap = new HashMap<>();
    //        if (list != null) {
    //            list.forEach(item ->
    // assMap.put(Long.valueOf(String.valueOf(item.get("assignee"))),
    // Integer.valueOf(String.valueOf(item.get("count")))));
    //        }
    //        HashMap<Long, Integer> empMap = new HashMap<>();
    //        employees.forEach(item -> empMap.put(item.getId(), assMap.getOrDefault(item.getId(),
    // 0)));
    //        return empMap.entrySet().stream().sorted(((o1, o2) ->
    // o1.getValue().compareTo(o2.getValue()))).findFirst().get().getKey();
    return 0L;
  }
}
