package cn.asany.pim.product.dao;

import cn.asany.pim.product.domain.ProductSpecification;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.productSpecificationDao")
public interface ProductSpecificationDao extends AnyJpaRepository<ProductSpecification, Long> {}
