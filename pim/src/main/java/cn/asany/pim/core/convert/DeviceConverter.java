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

import cn.asany.pim.core.domain.Device;
import cn.asany.pim.core.domain.WarrantyCard;
import cn.asany.pim.core.domain.WarrantyPolicy;
import cn.asany.pim.core.domain.enums.WarrantyStatus;
import cn.asany.pim.core.graphql.input.DeviceCreateInput;
import cn.asany.pim.core.graphql.input.DeviceUpdateInput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DeviceConverter {

  @Mappings({
    @Mapping(source = "productId", target = "product.id"),
  })
  Device toDevice(DeviceCreateInput input);

  @Mappings({})
  Device toDevice(DeviceUpdateInput input);

  @AfterMapping
  default void customizeMapping(@MappingTarget Device target, DeviceCreateInput source) {
    List<WarrantyCard> warrantyCards = new ArrayList<>();
    WarrantyCard warrantyCard = new WarrantyCard();
    warrantyCard.setPolicy(WarrantyPolicy.builder().id(source.getWarrantyPolicyId()).build());
    warrantyCard.setStartDate(source.getWarrantyStartDate());
    warrantyCard.setEndDate(source.getWarrantyEndDate());

    if (source.getWarrantyEndDate().before(new Date())) {
      warrantyCard.setStatus(WarrantyStatus.EXPIRED);
    } else {
      warrantyCard.setStatus(WarrantyStatus.ACTIVE);
    }

    warrantyCards.add(warrantyCard);
    target.setWarrantyCards(warrantyCards);
  }

  @AfterMapping
  default void customizeMapping(@MappingTarget Device target, DeviceUpdateInput source) {
    List<WarrantyCard> warrantyCards = new ArrayList<>();
    WarrantyCard warrantyCard = new WarrantyCard();
    warrantyCard.setPolicy(WarrantyPolicy.builder().id(source.getWarrantyPolicyId()).build());
    warrantyCard.setStartDate(source.getWarrantyStartDate());
    warrantyCard.setEndDate(source.getWarrantyEndDate());

    if (source.getWarrantyEndDate().before(new Date())) {
      warrantyCard.setStatus(WarrantyStatus.EXPIRED);
    } else {
      warrantyCard.setStatus(WarrantyStatus.ACTIVE);
    }

    warrantyCards.add(warrantyCard);
    target.setWarrantyCards(warrantyCards);
  }
}
