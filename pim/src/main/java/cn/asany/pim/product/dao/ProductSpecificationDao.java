package cn.asany.pim.product.dao;

import cn.asany.pim.product.domain.ProductSpecification;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.productSpecificationDao")
public interface ProductSpecificationDao extends JpaRepository<ProductSpecification, Long> {}
