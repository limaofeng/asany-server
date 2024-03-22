package cn.asany.pim.product.service;

import cn.asany.pim.product.dao.ProductLinkageTypeDao;
import org.springframework.stereotype.Service;

@Service
public class ProductLinkageTypeService {

  private final ProductLinkageTypeDao productLinkageTypeDao;

  public ProductLinkageTypeService(ProductLinkageTypeDao productLinkageTypeDao) {
    this.productLinkageTypeDao = productLinkageTypeDao;
  }
}
