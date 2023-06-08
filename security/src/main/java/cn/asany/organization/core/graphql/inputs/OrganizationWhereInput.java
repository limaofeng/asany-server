package cn.asany.organization.core.graphql.inputs;

import cn.asany.organization.core.domain.Organization;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.WhereInput;

/**
 * 组织筛选器
 *
 * @author limaofeng
 * @version V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrganizationWhereInput extends WhereInput<OrganizationWhereInput, Organization> {}
