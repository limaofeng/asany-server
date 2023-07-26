package cn.asany.organization.core.graphql;

import cn.asany.organization.core.convert.OrganizationConverter;
import cn.asany.organization.core.domain.Department;
import cn.asany.organization.core.domain.Organization;
import cn.asany.organization.core.graphql.inputs.DepartmentWhereInput;
import cn.asany.organization.core.graphql.inputs.OrganizationWhereInput;
import cn.asany.organization.core.graphql.inputs.UpdateOrganizationProfileUpdateInput;
import cn.asany.organization.core.graphql.resolvers.OrganizationGraphQLResolver;
import cn.asany.organization.core.service.DepartmentService;
import cn.asany.organization.core.service.EmployeeGroupService;
import cn.asany.organization.core.service.OrganizationService;
import cn.asany.organization.employee.service.EmployeeService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.util.regexp.RegexpConstant;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 组织
 *
 * @author limaofeng
 * @version V1.0
 */
@Component
public class OrganizationGraphQLQueryAndMutationResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final OrganizationService organizationService;
  private final DepartmentService departmentService;
  private final EmployeeService employeeService;
  private final EmployeeGroupService employeeGroupService;
  private final OrganizationGraphQLResolver organizationGraphQLResolver;
  @Autowired private OrganizationConverter organizationConverter;

  public OrganizationGraphQLQueryAndMutationResolver(
      OrganizationService organizationService,
      DepartmentService departmentService,
      EmployeeService employeeService,
      EmployeeGroupService employeeGroupService,
      OrganizationGraphQLResolver organizationGraphQLResolver) {
    this.organizationService = organizationService;
    this.departmentService = departmentService;
    this.employeeService = employeeService;
    this.employeeGroupService = employeeGroupService;
    this.organizationGraphQLResolver = organizationGraphQLResolver;
  }

  public List<Organization> organizations(OrganizationWhereInput where) {
    PropertyFilter propertyFilter = where.toFilter();
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    propertyFilter.notEqual("code", Organization.DEFAULT_ORGANIZATION_CODE);
    if (user != null) {
      propertyFilter.equal("members.user.id", user.getUid());
    }
    return organizationService.findAll(propertyFilter);
  }

  public Optional<Organization> organization(String id) {
    if (RegexpUtil.isMatch(id, RegexpConstant.VALIDATOR_INTEGE)) {
      return organizationService.findById(Long.valueOf(id));
    }
    return organizationService.findByCode(id);
  }

  /**
   * 全部部门
   *
   * @param organization 组织
   * @param where 过滤器
   * @return List<Department> 部门集合
   */
  public List<Department> departments(String organization, DepartmentWhereInput where) {
    return null;
  }

  /**
   * 单个部门
   *
   * @param id ID or Code
   * @return Department 部门
   */
  public Optional<Department> department(String id) {
    return null;
  }

  public Organization updateOrganizationProfile(
      Long id, UpdateOrganizationProfileUpdateInput input) {
    Organization organization = this.organizationConverter.toOrganization(input);
    return this.organizationService.updateOrganizationProfile(id, organization);
  }

  public Organization renameOrganizationCode(Long id, String code) {
    return this.organizationService.renameOrganizationCode(id, code);
  }

  public Boolean deleteOrganization(Long id) {
    this.organizationService.deleteOrganization(id);
    return Boolean.TRUE;
  }
}
