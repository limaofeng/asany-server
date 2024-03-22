package cn.asany.pim.product.convert;

import cn.asany.pim.product.domain.Brand;
import cn.asany.pim.product.graphql.input.BrandCreateInput;
import cn.asany.pim.product.graphql.input.BrandUpdateInput;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface BrandConverter {
  Brand toBrand(BrandCreateInput input);

  Brand toBrand(BrandUpdateInput input);
}
