package cn.asany.organization.core.graphql.resolvers;

import cn.asany.organization.core.domain.Department;
import cn.asany.organization.core.domain.Job;
import cn.asany.organization.core.domain.Organization;
import cn.asany.organization.core.domain.OrganizationMember;
import cn.asany.organization.core.graphql.inputs.DepartmentWhereInput;
import cn.asany.organization.core.service.DepartmentService;
import cn.asany.organization.core.service.DepartmentTypeService;
import cn.asany.organization.core.service.JobService;
import cn.asany.organization.core.service.OrganizationMemberService;
import cn.asany.organization.employee.service.EmployeeService;
import cn.asany.security.core.domain.Role;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 组织查询
 *
 * @author limaofeng
 * @version V1.0
 */
@Component
public class OrganizationGraphQLResolver implements GraphQLResolver<Organization> {

  @Autowired private DepartmentService departmentService;
  @Autowired private EmployeeService employeeService;
  @Autowired private JobService jobService;
  @Autowired private DepartmentTypeService departmentTypeService;

  @Autowired private OrganizationMemberService organizationMemberService;

  public List<Job> jobs(Organization organization, Sort orderBy) {
    return jobService.findAll(organization.getId(), orderBy);
  }

  public List<Department> departments(Organization organization, DepartmentWhereInput where) {
    if (where == null) {
      where = new DepartmentWhereInput();
    }
    return departmentService.findAll(
        where.toFilter().equal("organization.id", organization.getId()));
  }

  //    public EmployeeConnection employees(Organization organization, EmployeeFilter filter, int
  // page, int pageSize, Sort orderBy) {
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
  //        Pageable pageable = PageRequest.of(page, pageSize, orderBy.toSort());
  //        return Kit.connection(employeeService.findPage(pageable, builder.build()),
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

  public Optional<Role> role(Organization organization, Long of) {
    LoginUser loginUser = SpringSecurityUtils.getCurrentUser();

    Long user = ObjectUtil.defaultValue(of, () -> loginUser != null ? loginUser.getUid() : null);
    if (user == null) {
      return Optional.empty();
    }

    return organizationMemberService
        .findOneByUserAndOrganization(user, organization.getId())
        .map(OrganizationMember::getRole);
  }
}
