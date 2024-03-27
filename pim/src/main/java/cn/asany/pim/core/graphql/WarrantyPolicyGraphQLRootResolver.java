package cn.asany.pim.core.graphql;

import cn.asany.pim.core.convert.WarrantyPolicyConverter;
import cn.asany.pim.core.domain.WarrantyPolicy;
import cn.asany.pim.core.service.WarrantyPolicyService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Component;

@Component
public class WarrantyPolicyGraphQLRootResolver
    implements GraphQLMutationResolver, GraphQLQueryResolver {
  private final WarrantyPolicyService warrantyPolicyService;
  private final WarrantyPolicyConverter warrantyPolicyConverter;

  public WarrantyPolicyGraphQLRootResolver(
      WarrantyPolicyService warrantyPolicyService,
      WarrantyPolicyConverter warrantyPolicyConverter) {
    this.warrantyPolicyService = warrantyPolicyService;
    this.warrantyPolicyConverter = warrantyPolicyConverter;
  }

  public List<WarrantyPolicy> warrantyPolicies(Long productId) {
    return this.warrantyPolicyService.findAll(
        PropertyFilter.newFilter().equal("product.id", productId));
  }
}
