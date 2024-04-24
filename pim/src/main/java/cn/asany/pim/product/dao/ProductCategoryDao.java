package cn.asany.pim.product.dao;

import cn.asany.pim.product.domain.ProductCategory;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.ProductCategoryDao")
public interface ProductCategoryDao extends AnyJpaRepository<ProductCategory, Long> {}
