package cn.asany.pim.core.dao;

import cn.asany.pim.core.domain.AssetType;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.assetTypeDao")
public interface AssetTypeDao extends JpaRepository<AssetType, Long> {}
