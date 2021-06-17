package cn.asany.nuwa.template.service;

import cn.asany.nuwa.app.bean.enums.RouteType;
import cn.asany.nuwa.template.bean.ApplicationTemplate;
import cn.asany.nuwa.template.bean.ApplicationTemplateRoute;
import cn.asany.nuwa.template.dao.ApplicationTemplateDao;
import cn.asany.nuwa.template.dao.ApplicationTemplateRouteDao;
import cn.asany.ui.resources.bean.Component;
import cn.asany.ui.resources.bean.enums.ComponentScope;
import cn.asany.ui.resources.dao.ComponentDao;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<ApplicationTemplate> findAll() {
        return this.applicationTemplateDao.findAll();
    }

    @Transactional
    public void deleteApplicationTemplate(Long applicationId) {
        List<ApplicationTemplateRoute> routes = this.applicationTemplateRouteDao.findAll(PropertyFilter.builder().equal("application.id", applicationId).isNotNull("component").build());
        Set<Long> componentIds = routes.stream().filter(item -> item.getComponent().getScope() != ComponentScope.ROUTE).map(item -> item.getComponent().getId()).collect(Collectors.toSet());
        // 删除应用模版
        applicationTemplateDao.deleteById(applicationId);
        // 删除对应的路由组件
        this.componentDao.deleteAllById(componentIds);
    }

    @Transactional
    public ApplicationTemplate createApplicationTemplate(ApplicationTemplate application) {
        setupDefault(application, application.getRoutes());
        return applicationTemplateDao.save(application);
    }

    private List<ApplicationTemplateRoute> setupDefault(ApplicationTemplate application, List<ApplicationTemplateRoute> routes) {
        return setupDefault(application, new ArrayList<>(routes), null);
    }

    private List<ApplicationTemplateRoute> setupDefault(ApplicationTemplate application, List<ApplicationTemplateRoute> routes, ApplicationTemplateRoute parent) {
        long index = 1;
        for (ApplicationTemplateRoute route : routes) {
            route.setApplication(application);
            route.setLevel(parent == null ? 0 : parent.getLevel() + 1);
            route.setEnabled(Boolean.TRUE);
            route.setType(ObjectUtil.defaultValue(route.getType(), RouteType.ROUTE));
            route.setAuthorized(ObjectUtil.defaultValue(route.getAuthorized(), Boolean.FALSE));
            route.setHideInMenu(ObjectUtil.defaultValue(route.getHideInMenu(), Boolean.FALSE));
            route.setHideChildrenInMenu(ObjectUtil.defaultValue(route.getHideChildrenInMenu(), Boolean.FALSE));
            route.setHideInBreadcrumb(ObjectUtil.defaultValue(route.getHideInBreadcrumb(), Boolean.FALSE));
            route.setIndex(index++);

            Optional<Component> optionalComponent = setupComponent(route.getComponent());
            if (optionalComponent.isPresent()) {
                route.setComponent(optionalComponent.get());
            } else {
                route.setComponent(null);
            }

            if (route.getRoutes() != null) {
                setupDefault(application, new ArrayList<>(route.getRoutes()), route);
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
        if (component.getScope() != ComponentScope.ROUTE) {
            component.setScope(ComponentScope.ROUTE);
        }
        return Optional.of(this.componentDao.save(component));
    }

}
