package cn.asany.nuwa.template.dao;

import cn.asany.nuwa.template.bean.ApplicationTemplateRoute;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationTemplateRouteDao
    extends JpaRepository<ApplicationTemplateRoute, Long> {}
