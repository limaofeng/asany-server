package cn.asany.pim.core.dao;

import cn.asany.pim.core.domain.Device;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.assetDao")
public interface AssetDao extends JpaRepository<Device, Long> {}
