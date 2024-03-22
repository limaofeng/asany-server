package cn.asany.pim.product.service;

import cn.asany.pim.product.dao.BrandDao;
import cn.asany.pim.product.domain.Brand;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BrandService {

  private final BrandDao brandDao;

  public BrandService(BrandDao brandDao) {
    this.brandDao = brandDao;
  }

  public Page<Brand> findPage(Pageable pageable, PropertyFilter filter) {
    return this.brandDao.findPage(pageable, filter);
  }

  public List<Brand> findAll(PropertyFilter filter, int offset, int limit, Sort sort) {
    return this.brandDao.findAll(filter, offset, limit, sort);
  }

  public Optional<Brand> findById(String id) {
    return this.brandDao.findById(id);
  }

  public Brand save(Brand brand) {
    return this.brandDao.save(brand);
  }

  public Brand update(String id, Brand brand, Boolean merge) {
    brand.setId(id);
    return this.brandDao.update(brand, merge);
  }
}
