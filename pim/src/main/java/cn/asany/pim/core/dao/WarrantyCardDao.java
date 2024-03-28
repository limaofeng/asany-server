package cn.asany.pim.core.dao;

import cn.asany.pim.core.domain.WarrantyCard;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarrantyCardDao extends JpaRepository<WarrantyCard, Long> {}
