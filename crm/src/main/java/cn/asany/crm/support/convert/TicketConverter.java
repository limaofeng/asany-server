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
package cn.asany.crm.support.convert;

import cn.asany.crm.support.domain.Ticket;
import cn.asany.crm.support.graphql.input.TicketCreateInput;
import cn.asany.crm.support.graphql.input.TicketUpdateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TicketConverter {

  @Mappings({
    @Mapping(source = "type", target = "type.id"),
    @Mapping(source = "storeId", target = "store.id"),
    @Mapping(source = "customerId", target = "customer.id"),
  })
  Ticket toTicket(TicketCreateInput input);

  Ticket toTicket(TicketUpdateInput input);
}
