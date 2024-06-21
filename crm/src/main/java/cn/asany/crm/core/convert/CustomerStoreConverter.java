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
package cn.asany.crm.core.convert;

import cn.asany.crm.core.domain.CustomerStore;
import cn.asany.crm.core.graphql.input.CustomerStoreCreateInput;
import cn.asany.crm.core.graphql.input.CustomerStoreUpdateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CustomerStoreConverter {

  @Mappings(value = @Mapping(source = "customer", target = "customer.id"))
  CustomerStore toCustomerStore(CustomerStoreCreateInput input);

  CustomerStore toCustomerStore(CustomerStoreUpdateInput input);
}
