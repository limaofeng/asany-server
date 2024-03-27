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
