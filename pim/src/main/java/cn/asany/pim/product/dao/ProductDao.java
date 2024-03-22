package cn.asany.pim.product.dao;

import cn.asany.pim.product.domain.Product;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.ProductDao")
public interface ProductDao extends JpaRepository<Product, Long> {}
