package cn.asany.pim.product.service;

import cn.asany.pim.product.dao.ProductArticleDao;
import org.springframework.stereotype.Service;

@Service
public class ProductArticleService {

  private final ProductArticleDao productArticleDao;

  public ProductArticleService(ProductArticleDao productArticleDao) {
    this.productArticleDao = productArticleDao;
  }
}
