package cn.asany.pim.product.service;

import cn.asany.pim.product.dao.ProductVariantDao;
import org.springframework.stereotype.Service;

@Service
public class ProductModelService {

  private final ProductVariantDao productVariantDao;

  public ProductModelService(ProductVariantDao productVariantDao) {
    this.productVariantDao = productVariantDao;
  }
}
