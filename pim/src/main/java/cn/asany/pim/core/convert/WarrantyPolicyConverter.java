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
