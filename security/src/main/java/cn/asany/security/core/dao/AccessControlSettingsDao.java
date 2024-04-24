package cn.asany.security.core.dao;

import cn.asany.security.core.domain.AccessControlSettings;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessControlSettingsDao extends AnyJpaRepository<AccessControlSettings, Long> {}
