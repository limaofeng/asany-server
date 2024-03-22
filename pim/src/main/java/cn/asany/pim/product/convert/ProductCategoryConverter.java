package cn.asany.pim.product.convert;

import cn.asany.pim.product.domain.ProductCategory;
import cn.asany.pim.product.graphql.input.ProductCategoryCreateInput;
import cn.asany.pim.product.graphql.input.ProductCategoryUpdateInput;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ProductCategoryConverter {
  ProductCategory toProductCategory(ProductCategoryCreateInput input);

  ProductCategory toProductCategory(ProductCategoryUpdateInput input);
}
