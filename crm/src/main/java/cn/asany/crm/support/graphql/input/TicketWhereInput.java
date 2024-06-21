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
package cn.asany.crm.support.graphql.input;

import cn.asany.base.common.TicketTargetType;
import cn.asany.crm.support.domain.Ticket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.graphql.inputs.WhereInput;

@Data
@EqualsAndHashCode(callSuper = true)
public class TicketWhereInput extends WhereInput<TicketWhereInput, Ticket> {

  public void setKeyword(String keyword) {
    filter.or(
        PropertyFilter.newFilter().contains("description", keyword),
        PropertyFilter.newFilter().contains("no", keyword));
  }

  public void setCustomerId(Long customerId) {
    filter.equal("customer.id", customerId);
  }

  public void setStoreId(Long storeId) {
    filter.equal("store.id", storeId);
  }

  public void setDeviceId(Long deviceId) {
    filter.equal("target.id", deviceId).equal("target.type", TicketTargetType.DEVICE);
  }

  public void setTarget(TicketTargetInput target) {
    filter.equal("target.id", target.getId()).equal("target.type", target.getType());
  }

  public void setAssignee(Long assigneeId) {
    filter.equal("assignee.id", assigneeId);
  }

  public void setType(Long typeId) {
    filter.equal("type.id", typeId);
  }
}
