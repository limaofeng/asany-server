package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.domain.ApplicationDependency;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationDependencyDao extends JpaRepository<ApplicationDependency, Long> {}
