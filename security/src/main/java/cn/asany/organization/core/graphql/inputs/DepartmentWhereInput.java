package cn.asany.organization.core.graphql.inputs;

import cn.asany.organization.core.domain.Organization;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.WhereInput;

/**
 * 部门筛选器
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DepartmentWhereInput extends WhereInput<OrganizationWhereInput, Organization> {}
