package cn.asany.cms.learn.graphql.resolver;

import cn.asany.cms.learn.bean.LearnerScope;
import cn.asany.cms.learn.dao.LearnerDao;
import cn.asany.cms.learn.service.LearnerService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LearnerScopeGraphQLResolver implements GraphQLResolver<LearnerScope> {

  @Autowired private LearnerGraphQLResolver learnerGraphQLResolver;

  @Autowired private LearnerService learnerService;

  @Autowired private LearnerDao learnerDao;
  // TODO
  public float lengthStudy(LearnerScope scope) {
    //        if (scope.getScope().startsWith("EMPLOYEE")) {
    //            String employee = StringUtils.substringAfterLast(scope.getScope(), "_");
    //            return this.learnerService.lengthStudy(scope.getCourse(), Long.valueOf(employee));
    //        } else if (scope.getScope().startsWith("DEPARTMENT")) {
    //            String department = StringUtils.substringAfterLast(scope.getScope(), "_");
    //            List<EmployeePosition> byDepartment =
    // employeePositionDao.findByDepartment(Department.builder().id(Long.valueOf(department)).build());
    //            for (EmployeePosition employeePosition : byDepartment) {
    //                return this.learnerService.lengthStudy(scope.getCourse(),
    // Long.valueOf(employeePosition.getEmployee().getId()));
    //            }
    //        } else if (scope.getScope().startsWith("ORGANIZATION")) {
    //            String organization = StringUtils.substringAfterLast(scope.getScope(), "_");
    //            List<EmployeePosition> employeePositions =
    // employeePositionDao.findByOrganization(Organization.builder().id(organization).build());
    //            for (EmployeePosition employeePosition : employeePositions) {
    //                return this.learnerService.lengthStudy(scope.getCourse(),
    // Long.valueOf(employeePosition.getEmployee().getId()));
    //            }
    //        }
    return 0;
  }

  public Date lastStudyTime(LearnerScope scope, int page, int pageSize) {
    //        if (scope.getScope().startsWith("EMPLOYEE")) {
    //            String employeeId = StringUtils.substringAfterLast(scope.getScope(), "_");
    //            Employee employee = Employee.builder().id(Long.valueOf(employeeId)).build();
    //            Learner byCourseAndEmployee =
    // learnerDao.findByCourseAndEmployee(scope.getCourse(), employee);
    //            if (byCourseAndEmployee == null) {
    //                return null;
    //            }
    //            return this.learnerGraphQLResolver.lastStudyTime(byCourseAndEmployee, page,
    // pageSize);
    //        } else if (scope.getScope().startsWith("DEPARTMENT")) {
    //            String department = StringUtils.substringAfterLast(scope.getScope(), "_");
    //            List<EmployeePosition> byDepartment =
    // employeePositionDao.findByDepartment(Department.builder().id(Long.valueOf(department)).build());
    //            for (EmployeePosition employeePosition : byDepartment) {
    //                Employee employee = employeePosition.getEmployee();
    //                Learner byCourseAndEmployee =
    // learnerDao.findByCourseAndEmployee(scope.getCourse(), employee);
    //                if (byCourseAndEmployee == null) {
    //                    return null;
    //                }
    //                return this.learnerGraphQLResolver.lastStudyTime(byCourseAndEmployee, page,
    // pageSize);
    //            }
    //        } else if (scope.getScope().startsWith("ORGANIZATION")) {
    //            String organization = StringUtils.substringAfterLast(scope.getScope(), "_");
    //            List<EmployeePosition> employeePositions =
    // employeePositionDao.findByOrganization(Organization.builder().id(organization).build());
    //            for (EmployeePosition employeePosition : employeePositions) {
    //                Employee employee = employeePosition.getEmployee();
    //                Learner byCourseAndEmployee =
    // learnerDao.findByCourseAndEmployee(scope.getCourse(), employee);
    //                if (byCourseAndEmployee == null) {
    //                    return null;
    //                }
    //                return this.learnerGraphQLResolver.lastStudyTime(byCourseAndEmployee, page,
    // pageSize);
    //            }
    //        }
    return null;
  }

  public Integer learningProgress(LearnerScope scope) {
    //        if (scope.getScope().startsWith("EMPLOYEE")) {
    //            String employeeId = StringUtils.substringAfterLast(scope.getScope(), "_");
    //            Employee employee = Employee.builder().id(Long.valueOf(employeeId)).build();
    //            Learner byCourseAndEmployee =
    // learnerDao.findByCourseAndEmployee(scope.getCourse(), employee);
    //            if (byCourseAndEmployee == null) {
    //                return null;
    //            }
    //            return byCourseAndEmployee.getLearningProgress();
    //        } else if (scope.getScope().startsWith("DEPARTMENT")) {
    //            String department = StringUtils.substringAfterLast(scope.getScope(), "_");
    //            List<EmployeePosition> byDepartment =
    // employeePositionDao.findByDepartment(Department.builder().id(Long.valueOf(department)).build());
    //            for (EmployeePosition employeePosition : byDepartment) {
    //                Employee employee = employeePosition.getEmployee();
    //                Learner byCourseAndEmployee =
    // learnerDao.findByCourseAndEmployee(scope.getCourse(), employee);
    //                if (byCourseAndEmployee == null) {
    //                    return null;
    //                }
    //                return byCourseAndEmployee.getLearningProgress();
    //            }
    //        } else if (scope.getScope().startsWith("ORGANIZATION")) {
    //            String organization = StringUtils.substringAfterLast(scope.getScope(), "_");
    //            List<EmployeePosition> employeePositions =
    // employeePositionDao.findByOrganization(Organization.builder().id(organization).build());
    //            for (EmployeePosition employeePosition : employeePositions) {
    //                Employee employee = employeePosition.getEmployee();
    //                Learner byCourseAndEmployee =
    // learnerDao.findByCourseAndEmployee(scope.getCourse(), employee);
    //                if (byCourseAndEmployee == null) {
    //                    return null;
    //                }
    //                return byCourseAndEmployee.getLearningProgress();
    //            }
    //        }
    return null;
  }
}
