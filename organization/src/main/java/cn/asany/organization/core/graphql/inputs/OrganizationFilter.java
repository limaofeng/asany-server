package cn.asany.organization.core.graphql.inputs;

import cn.asany.organization.core.bean.Organization;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.QueryFilter;

/**
 * 组织筛选器
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019/9/6 11:45 上午
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrganizationFilter extends QueryFilter<OrganizationFilter, Organization> {
}
