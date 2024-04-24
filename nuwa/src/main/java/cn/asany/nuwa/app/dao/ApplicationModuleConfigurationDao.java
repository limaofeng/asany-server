package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.domain.ApplicationModuleConfiguration;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationModuleConfigurationDao
    extends AnyJpaRepository<ApplicationModuleConfiguration, Long> {}
