package cn.asany.cms.learn.service;

import cn.asany.cms.learn.dao.LearnerScopeDao;
import cn.asany.cms.learn.domain.Course;
import cn.asany.cms.learn.domain.LearnerScope;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("learnerScopeService")
public class LearnerScopeService {

  @Autowired private LearnerScopeDao learnerScopeDao;

  public List<LearnerScope> findLearnerScopeByCourseId(Course course) {
    return learnerScopeDao.findByCourse(course);
  }

  public Page<LearnerScope> findPage(Pageable pageable, List<PropertyFilter> filters) {
    Page<LearnerScope> pager1 = learnerScopeDao.findPage(pageable, filters);
    List<LearnerScope> pageItems = pager1.getContent();
    List<LearnerScope> learnerScopes = new ArrayList<>();
    //        for(LearnerScope pageItem : pageItems){
    //            String scope = pageItem.getScope();
    //            if (pageItem.getScope().startsWith("EMPLOYEE")) {
    //                String employee = StringUtils.substringAfterLast(scope, "_");
    //                Employee e = organizationGrpcInvoke.employee(Long.valueOf(employee));
    //                pageItem.setEmployee(e);
    //                learnerScopes.add(pageItem);
    //            } else if (pageItem.getScope().startsWith("DEPARTMENT")) {
    //                String departmentId = StringUtils.substringAfterLast(pageItem.getScope(),
    // "_");
    //                Department department1 =
    // organizationGrpcInvoke.department(Long.valueOf(departmentId));
    //                for (EmployeePosition employeePosition : department1.getEmployees()) {
    //                    Employee employee = employeePosition.getEmployee();
    //                    pageItem.setEmployee(employee);
    //                    learnerScopes.add(pageItem);
    //                }
    //            }else if (pageItem.getScope().startsWith("ORGANIZATION")) {
    //                String organization = StringUtils.substringAfterLast(pageItem.getScope(),
    // "_");
    //                // TODO 获取组织下的所有人员
    ////                List<EmployeePosition> employeePositions =
    // employeePositionDao.findByOrganization(Organization.builder().id(organization).build());
    ////                for (EmployeePosition employeePosition : employeePositions) {
    ////                    Employee employee = employeePosition.getEmployee();
    ////                    pageItem.setEmployee(employee);
    ////                    learnerScopes.add(pageItem);
    ////                }
    //            }
    //        }
    return pager1;
  }

  public List<Course> findAllByLearnerScope(String scope) {
    return learnerScopeDao.findAll(Example.of(LearnerScope.builder().scope(scope).build())).stream()
        .map(LearnerScope::getCourse)
        .collect(Collectors.toList());
  }
}
