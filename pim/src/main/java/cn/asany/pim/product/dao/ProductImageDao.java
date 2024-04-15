package cn.asany.pim.product.dao;

import cn.asany.pim.product.domain.ProductImage;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.ProductImageDao")
public interface ProductImageDao extends JpaRepository<ProductImage, Long> {}
