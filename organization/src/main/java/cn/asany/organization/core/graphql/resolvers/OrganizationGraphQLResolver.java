package cn.asany.organization.core.graphql.resolvers;

import cn.asany.organization.core.bean.Department;
import cn.asany.organization.core.bean.Job;
import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.core.graphql.inputs.DepartmentFilter;
import cn.asany.organization.core.service.DepartmentService;
import cn.asany.organization.core.service.DepartmentTypeService;
import cn.asany.organization.core.service.JobService;
import cn.asany.organization.employee.service.EmployeeService;
import cn.asany.organization.relationship.service.OrganizationEmployeeService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import org.jfantasy.framework.dao.OrderBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-04-02 12:01
 */
@Component
public class OrganizationGraphQLResolver implements GraphQLResolver<Organization> {

  @Autowired private DepartmentService departmentService;
  @Autowired private EmployeeService employeeService;
  //    @Autowired
  //    private RoleService roleService;
  @Autowired private JobService jobService;
  @Autowired private DepartmentTypeService departmentTypeService;
  @Autowired private OrganizationEmployeeService organizationEmployeeService;

  public List<Job> jobs(Organization organization, OrderBy orderBy) {
    return jobService.findAll(organization.getId(), orderBy);
  }

  public List<Department> departments(Organization organization, DepartmentFilter filter) {
    if (filter == null) {
      filter = new DepartmentFilter();
    }
    return departmentService.findAll(
        filter.getBuilder().equal("organization.id", organization.getId()).build());
  }

  //    public EmployeeConnection employees(Organization organization, EmployeeFilter filter, int
  // page, int pageSize, OrderBy orderBy) {
  //        PropertyFilterBuilder builder = PropertyFilter.builder();
  //        if (organization.getEmployeeFilter() != null) {
  //            builder.and(organization.getEmployeeFilter().getBuilder());
  //        }
  //        if (filter != null) {
  //            if (StringUtils.isEmpty(filter.getStatus())){
  //                filter.setStatus("normal");
  //            }
  //            builder.and(filter.getBuilder());
  //        }
  //        builder.equal("organizationEmployees.organization.id", organization.getId());
  //        Pager<Employee> pager = new Pager<>(page, pageSize, orderBy);
  //        return Kit.connection(employeeService.findPager(pager, builder.build()),
  // EmployeeConnection.class, new EmployeeEdgeConverter(item -> {
  //            item.setCurrentOrganization(organization);
  //            return item;
  //        }));
  //    }

  //    public List<Role> roles(Organization organization, Boolean enabled,String scope) {
  //        return roleService.getAllByOrg(organization.getId(),
  // RoleScope.builder().id(scope).build());
  //    }

  //    public List<DepartmentType> departmentTypes(Organization organization) {
  //        return departmentTypeService.selectDepartmentTypeByOrganization(organization.getId());
  //    }
}
