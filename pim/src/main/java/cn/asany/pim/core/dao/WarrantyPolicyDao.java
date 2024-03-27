package cn.asany.pim.core.dao;

import cn.asany.pim.core.domain.WarrantyPolicy;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.WarrantyPolicyDao")
public interface WarrantyPolicyDao extends JpaRepository<WarrantyPolicy, Long> {}
