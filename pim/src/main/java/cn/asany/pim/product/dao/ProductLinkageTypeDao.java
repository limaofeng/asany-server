package cn.asany.pim.product.dao;

import cn.asany.pim.product.domain.ProductLinkageType;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.ProductLinkageTypeDao")
public interface ProductLinkageTypeDao extends AnyJpaRepository<ProductLinkageType, Long> {}
