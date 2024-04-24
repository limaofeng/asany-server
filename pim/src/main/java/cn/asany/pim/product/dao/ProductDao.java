package cn.asany.pim.product.dao;

import cn.asany.pim.product.domain.Product;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.ProductDao")
public interface ProductDao extends AnyJpaRepository<Product, Long> {}
