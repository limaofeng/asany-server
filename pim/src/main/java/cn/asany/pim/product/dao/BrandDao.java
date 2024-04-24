package cn.asany.pim.product.dao;

import cn.asany.pim.product.domain.Brand;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.brandDao")
public interface BrandDao extends AnyJpaRepository<Brand, String> {}
