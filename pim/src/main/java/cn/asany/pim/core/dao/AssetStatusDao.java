package cn.asany.pim.core.dao;

import cn.asany.pim.core.domain.AssetStatus;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pim.assetStatusDao")
public interface AssetStatusDao extends JpaRepository<AssetStatus, Long> {}
