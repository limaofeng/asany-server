package cn.asany.pim.product.dao;

import cn.asany.pim.product.domain.ProductLinkageType;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.ProductLinkageTypeDao")
public interface ProductLinkageTypeDao extends JpaRepository<ProductLinkageType, Long> {}
