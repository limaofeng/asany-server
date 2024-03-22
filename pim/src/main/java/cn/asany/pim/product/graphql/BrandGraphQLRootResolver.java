package cn.asany.pim.product.graphql;

import cn.asany.pim.product.convert.BrandConverter;
import cn.asany.pim.product.domain.Brand;
import cn.asany.pim.product.graphql.input.BrandCreateInput;
import cn.asany.pim.product.graphql.input.BrandUpdateInput;
import cn.asany.pim.product.graphql.input.BrandWhereInput;
import cn.asany.pim.product.graphql.type.BrandConnection;
import cn.asany.pim.product.service.BrandService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class BrandGraphQLRootResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final BrandService brandService;

  private final BrandConverter brandConverter;

  public BrandGraphQLRootResolver(BrandService brandService, BrandConverter brandConverter) {
    this.brandService = brandService;
    this.brandConverter = brandConverter;
  }

  public BrandConnection brandsConnection(
      BrandWhereInput where, int pageNumber, int pageSize, Sort orderBy) {
    PropertyFilter filter = ObjectUtil.defaultValue(where, BrandWhereInput::new).toFilter();
    Page<Brand> page =
        brandService.findPage(PageRequest.of(pageNumber - 1, pageSize, orderBy), filter);
    return Kit.connection(page, BrandConnection.class);
  }

  public List<Brand> brands(BrandWhereInput where, int offset, int limit, Sort sort) {
    PropertyFilter filter = ObjectUtil.defaultValue(where, BrandWhereInput::new).toFilter();
    return brandService.findAll(filter, offset, limit, sort);
  }

  public Optional<Brand> brand(String id) {
    return brandService.findById(id);
  }

  public Brand createBrand(BrandCreateInput input) {
    Brand brand = brandConverter.toBrand(input);
    return this.brandService.save(brand);
  }

  public Brand updateBrand(String id, BrandUpdateInput input, Boolean merge) {
    Brand brand = brandConverter.toBrand(input);
    return this.brandService.update(id, brand, merge);
  }
}
