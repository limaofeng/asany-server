package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelFieldMetadataDao extends AnyJpaRepository<ModelFieldMetadata, Long> {}
