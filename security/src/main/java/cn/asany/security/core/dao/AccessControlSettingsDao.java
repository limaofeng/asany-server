package cn.asany.security.core.dao;

import cn.asany.security.core.domain.AccessControlSettings;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessControlSettingsDao extends JpaRepository<AccessControlSettings, Long> {}
