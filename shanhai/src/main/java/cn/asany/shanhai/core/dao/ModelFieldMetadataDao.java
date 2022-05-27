package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelFieldMetadataDao extends JpaRepository<ModelFieldMetadata, Long> {}
