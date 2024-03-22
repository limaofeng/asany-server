package cn.asany.pim.product.dao;

import cn.asany.pim.product.domain.ProductVariant;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.ProductVariantDao")
public interface ProductVariantDao extends JpaRepository<ProductVariant, Long> {}
