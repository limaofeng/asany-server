package cn.asany.nuwa.template.service;

import cn.asany.nuwa.app.bean.enums.RouteType;
import cn.asany.nuwa.template.bean.ApplicationTemplate;
import cn.asany.nuwa.template.bean.ApplicationTemplateRoute;
import cn.asany.nuwa.template.dao.ApplicationTemplateDao;
import cn.asany.nuwa.template.dao.ApplicationTemplateRouteDao;
import cn.asany.ui.resources.bean.Component;
import cn.asany.ui.resources.dao.ComponentDao;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationTemplateService {

    private final ComponentDao componentDao;
    private final ApplicationTemplateDao applicationTemplateDao;
    private final ApplicationTemplateRouteDao applicationTemplateRouteDao;

    public ApplicationTemplateService(ApplicationTemplateDao applicationTemplateDao, ApplicationTemplateRouteDao applicationTemplateRouteDao, ComponentDao componentDao) {
        this.componentDao = componentDao;
        this.applicationTemplateDao = applicationTemplateDao;
        this.applicationTemplateRouteDao = applicationTemplateRouteDao;
    }

    public ApplicationTemplate createApplicationTemplate(ApplicationTemplate template) {
        setupDefault(template.getRoutes());
        return applicationTemplateDao.save(template);
    }

    private List<ApplicationTemplateRoute> setupDefault(List<ApplicationTemplateRoute> routes) {
        return setupDefault(routes, null);
    }

    private List<ApplicationTemplateRoute> setupDefault(List<ApplicationTemplateRoute> routes, ApplicationTemplateRoute parent) {
        for (ApplicationTemplateRoute route : routes) {
            route.setLevel(parent == null ? 0 : parent.getLevel() + 1);
            route.setEnabled(Boolean.TRUE);
            route.setType(ObjectUtil.defaultValue(route.getType(), RouteType.ROUTE));
            route.setAuthorized(ObjectUtil.defaultValue(route.getAuthorized(), Boolean.FALSE));
            route.setHideInMenu(ObjectUtil.defaultValue(route.getHideInMenu(), Boolean.FALSE));
            route.setHideChildrenInMenu(ObjectUtil.defaultValue(route.getHideChildrenInMenu(), Boolean.FALSE));
            route.setHideInBreadcrumb(ObjectUtil.defaultValue(route.getHideInBreadcrumb(), Boolean.FALSE));

            Optional<Component> optionalComponent = setupComponent(route.getComponent());
            if (optionalComponent.isPresent()) {
                route.setComponent(optionalComponent.get());
            } else {
                route.setComponent(null);
            }

            this.applicationTemplateRouteDao.save(route);

            if (route.getRoutes() != null) {
                setupDefault(route.getRoutes());
            }

        }
        return routes;
    }

    private Optional<Component> setupComponent(Component component) {
        if (component == null) {
            return Optional.empty();
        }
        if (component.getCode() != null) {
            return this.componentDao.findOne(PropertyFilter.builder().equal("code", component.getCode()).build());
        }
        return Optional.of(this.componentDao.save(component));
    }

}
