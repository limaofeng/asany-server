package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.domain.ModelMetadata;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelMetadataDao extends JpaRepository<ModelMetadata, Long> {}
