package cn.asany.pim.product.service;

import cn.asany.pim.product.dao.ProductSpecificationDao;
import org.springframework.stereotype.Service;

@Service
public class ProductVariantService {

  private final ProductSpecificationDao productSpecificationDao;

  public ProductVariantService(ProductSpecificationDao productSpecificationDao) {
    this.productSpecificationDao = productSpecificationDao;
  }
}
