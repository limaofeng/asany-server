package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.domain.ApplicationDependency;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationDependencyDao extends AnyJpaRepository<ApplicationDependency, Long> {}
