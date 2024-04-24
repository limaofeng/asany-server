package cn.asany.pim.product.dao;

import cn.asany.pim.product.domain.ProductImage;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.ProductImageDao")
public interface ProductImageDao extends AnyJpaRepository<ProductImage, Long> {}
