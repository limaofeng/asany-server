package cn.asany.pim.core.convert;

import cn.asany.pim.core.domain.WarrantyPolicy;
import cn.asany.pim.core.graphql.input.WarrantyPolicyCreateInput;
import cn.asany.pim.core.graphql.input.WarrantyPolicyUpdateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface WarrantyPolicyConverter {
  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "product.id", source = "productId"),
    @Mapping(target = "name", source = "name"),
    @Mapping(target = "duration", source = "duration")
  })
  WarrantyPolicy toWarrantyPolicy(WarrantyPolicyCreateInput input);

  @Mappings({
    @Mapping(target = "name", source = "name"),
    @Mapping(target = "duration", source = "duration")
  })
  WarrantyPolicy toWarrantyPolicy(WarrantyPolicyUpdateInput input);
}
