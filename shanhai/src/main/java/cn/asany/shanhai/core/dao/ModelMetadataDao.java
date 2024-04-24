package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.domain.ModelMetadata;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelMetadataDao extends AnyJpaRepository<ModelMetadata, Long> {}
