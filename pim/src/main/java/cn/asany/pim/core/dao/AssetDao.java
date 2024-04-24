package cn.asany.pim.core.dao;

import cn.asany.pim.core.domain.Device;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.assetDao")
public interface AssetDao extends AnyJpaRepository<Device, Long> {}
