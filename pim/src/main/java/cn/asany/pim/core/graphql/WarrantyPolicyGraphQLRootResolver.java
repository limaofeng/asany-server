package cn.asany.pim.core.graphql;

import cn.asany.base.common.BatchPayload;
import cn.asany.pim.core.convert.WarrantyPolicyConverter;
import cn.asany.pim.core.domain.WarrantyPolicy;
import cn.asany.pim.core.graphql.input.WarrantyPolicyCreateInput;
import cn.asany.pim.core.graphql.input.WarrantyPolicyUpdateInput;
import cn.asany.pim.core.graphql.input.WarrantyPolicyWhereInput;
import cn.asany.pim.core.service.WarrantyPolicyService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
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

  public Optional<WarrantyPolicy> warrantyPolicy(Long id) {
    return this.warrantyPolicyService.findById(id);
  }

  public WarrantyPolicy createWarrantyPolicy(WarrantyPolicyCreateInput input) {
    return this.warrantyPolicyService.save(warrantyPolicyConverter.toWarrantyPolicy(input));
  }

  public Optional<WarrantyPolicy> updateWarrantyPolicy(
      Long id, WarrantyPolicyUpdateInput input, Boolean merge) {
    return this.warrantyPolicyService.update(
        id, warrantyPolicyConverter.toWarrantyPolicy(input), merge);
  }

  public Optional<WarrantyPolicy> deleteWarrantyPolicy(Long id) {
    return this.warrantyPolicyService.delete(id);
  }

  public BatchPayload deleteManyWarrantyPolicies(WarrantyPolicyWhereInput where) {
    return BatchPayload.of(this.warrantyPolicyService.deleteMany(where.toFilter()));
  }
}
