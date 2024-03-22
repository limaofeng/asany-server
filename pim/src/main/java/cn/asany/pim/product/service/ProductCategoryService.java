package cn.asany.pim.product.service;

import cn.asany.pim.product.dao.ProductCategoryDao;
import cn.asany.pim.product.domain.ProductCategory;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryService {

  private final ProductCategoryDao productCategoryDao;

  public ProductCategoryService(ProductCategoryDao productCategoryDao) {
    this.productCategoryDao = productCategoryDao;
  }

  public List<ProductCategory> findAll(PropertyFilter filter, int offset, int limit, Sort sort) {
    return this.productCategoryDao.findAll(filter, offset, limit, sort);
  }

  public Optional<ProductCategory> findById(Long id) {
    return this.productCategoryDao.findById(id);
  }

  public ProductCategory save(ProductCategory productCategory) {
    return this.productCategoryDao.save(productCategory);
  }

  public ProductCategory update(Long id, ProductCategory productCategory, Boolean merge) {
    productCategory.setId(id);
    return this.productCategoryDao.update(productCategory, merge);
  }

  public Optional<ProductCategory> delete(Long id) {
    Optional<ProductCategory> category = this.productCategoryDao.findById(id);
    category.ifPresent(this.productCategoryDao::delete);
    return category;
  }
}
