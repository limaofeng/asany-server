package cn.asany.pim.product.dao;

import cn.asany.pim.product.domain.ProductVariant;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.ProductVariantDao")
public interface ProductVariantDao extends AnyJpaRepository<ProductVariant, Long> {}
