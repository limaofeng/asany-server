package cn.asany.pim.product.dao;

import cn.asany.pim.product.domain.Brand;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.brandDao")
public interface BrandDao extends JpaRepository<Brand, String> {}
