package cn.asany.pim.core.dao;

import cn.asany.pim.core.domain.AssetType;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.assetTypeDao")
public interface AssetTypeDao extends AnyJpaRepository<AssetType, Long> {}
