package cn.asany.nuwa.template.dao;

import cn.asany.nuwa.template.domain.ApplicationTemplateRoute;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationTemplateRouteDao
    extends AnyJpaRepository<ApplicationTemplateRoute, Long> {}
