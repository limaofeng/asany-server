package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.domain.ApplicationModuleConfiguration;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationModuleConfigurationDao
    extends JpaRepository<ApplicationModuleConfiguration, Long> {}
