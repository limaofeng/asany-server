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
}
