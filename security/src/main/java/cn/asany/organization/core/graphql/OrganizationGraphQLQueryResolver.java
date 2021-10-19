package cn.asany.organization.core.graphql;

import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.core.graphql.inputs.OrganizationFilter;
import cn.asany.organization.core.graphql.resolvers.OrganizationGraphQLResolver;
import cn.asany.organization.core.service.DepartmentService;
import cn.asany.organization.core.service.EmployeeGroupService;
import cn.asany.organization.core.service.OrganizationService;
import cn.asany.organization.employee.service.EmployeeService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 组织
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-04-01 15:17
 */
@Component
public class OrganizationGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired private OrganizationService organizationService;
  @Autowired private DepartmentService departmentService;
  @Autowired private EmployeeService employeeService;
  @Autowired private EmployeeGroupService employeeGroupService;
  @Autowired private OrganizationGraphQLResolver organizationGraphQLResolver;

  public List<Organization> organizations(OrganizationFilter filter) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new OrganizationFilter()).getBuilder();
    return organizationService.findAll(builder.build());
  }

  public Optional<Organization> organization(Long id) {
    return organizationService.findById(id);
  }
}
