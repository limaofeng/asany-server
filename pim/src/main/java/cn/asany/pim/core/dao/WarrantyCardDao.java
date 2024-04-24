package cn.asany.pim.core.dao;

import cn.asany.pim.core.domain.WarrantyCard;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarrantyCardDao extends AnyJpaRepository<WarrantyCard, Long> {}
