package cn.asany.crm.core.graphql.input;

import cn.asany.crm.core.domain.CustomerStore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.WhereInput;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerStoreWhereInput extends WhereInput<CustomerStoreWhereInput, CustomerStore> {}
