/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
