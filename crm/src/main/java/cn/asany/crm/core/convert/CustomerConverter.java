package cn.asany.crm.core.convert;

import cn.asany.crm.core.domain.Customer;
import cn.asany.crm.core.graphql.input.CustomerCreateInput;
import cn.asany.crm.core.graphql.input.CustomerUpdateInput;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CustomerConverter {
  Customer toCustomer(CustomerCreateInput input);

  Customer toCustomer(CustomerUpdateInput input);
}
