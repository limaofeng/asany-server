package cn.asany.pim.core.dao;

import cn.asany.pim.core.domain.WarrantyPolicy;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.WarrantyPolicyDao")
public interface WarrantyPolicyDao extends AnyJpaRepository<WarrantyPolicy, Long> {}
