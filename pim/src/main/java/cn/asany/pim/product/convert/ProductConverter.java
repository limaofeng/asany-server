package cn.asany.pim.product.convert;

import cn.asany.pim.product.domain.Product;
import cn.asany.pim.product.graphql.input.ProductCreateInput;
import cn.asany.pim.product.graphql.input.ProductUpdateInput;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ProductConverter {

  @Mappings({
    @Mapping(source = "brandId", target = "brand.id"),
  })
  Product toProduct(ProductCreateInput input);

  Product toProduct(ProductUpdateInput input);
}
