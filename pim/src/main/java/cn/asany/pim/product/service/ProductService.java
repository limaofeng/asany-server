package cn.asany.pim.product.service;

import cn.asany.pim.product.dao.ProductDao;
import cn.asany.pim.product.domain.Product;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final ProductDao productDao;

  public ProductService(ProductDao productDao) {
    this.productDao = productDao;
  }

  public Page<Product> findPage(Pageable pageable, PropertyFilter filter) {
    return this.productDao.findPage(pageable, filter);
  }

  public Product save(Product product) {
    return this.productDao.save(product);
  }

  public Product update(Long id, Product product, Boolean merge) {
    product.setId(id);
    return this.productDao.update(product, merge);
  }

  public List<Product> findAll(PropertyFilter filter, int offset, int limit, Sort sort) {
    return this.productDao.findAll(filter, offset, limit, sort);
  }

  public Optional<Product> findBySn(String id) {
    return this.productDao.findOneBy("sn", id);
  }

  public Optional<Product> findById(Long id) {
    return this.productDao.findById(id);
  }

  public Optional<Product> delete(Long id) {
    Optional<Product> product = this.productDao.findById(id);
    product.ifPresent(this.productDao::delete);
    return product;
  }
}
