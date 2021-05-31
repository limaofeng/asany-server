package cn.asany.organization.core.graphql.inputs;

import cn.asany.organization.core.bean.Organization;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.QueryFilter;

/**
 * 部门筛选器
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DepartmentFilter extends QueryFilter<OrganizationFilter, Organization> {

}
