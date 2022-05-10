package cn.asany.organization.core.convert;

import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.core.graphql.inputs.UpdateOrganizationProfileUpdateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface OrganizationConverter {

  /**
   * 组织个人资料
   *
   * @param input 个人资料
   * @return Organization
   */
  @Mappings({})
  Organization toOrganization(UpdateOrganizationProfileUpdateInput input);
}
