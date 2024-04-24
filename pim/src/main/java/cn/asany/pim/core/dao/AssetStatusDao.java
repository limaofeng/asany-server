package cn.asany.pim.core.dao;

import cn.asany.pim.core.domain.AssetStatus;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.assetStatusDao")
public interface AssetStatusDao extends AnyJpaRepository<AssetStatus, Long> {}
