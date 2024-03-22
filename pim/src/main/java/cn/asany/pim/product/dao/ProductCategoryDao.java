package cn.asany.pim.product.dao;

import cn.asany.pim.product.domain.ProductCategory;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.ProductCategoryDao")
public interface ProductCategoryDao extends JpaRepository<ProductCategory, Long> {}
