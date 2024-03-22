package cn.asany.pim.product.graphql.input;

import cn.asany.pim.product.domain.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.WhereInput;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductWhereInput extends WhereInput<ProductWhereInput, Product> {}
