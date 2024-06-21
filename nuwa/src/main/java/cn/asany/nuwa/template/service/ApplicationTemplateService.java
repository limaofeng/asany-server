/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.nuwa.template.service;

import cn.asany.nuwa.app.domain.enums.RouteType;
import cn.asany.nuwa.template.dao.ApplicationTemplateDao;
import cn.asany.nuwa.template.dao.ApplicationTemplateRouteDao;
import cn.asany.nuwa.template.domain.ApplicationTemplate;
import cn.asany.nuwa.template.domain.ApplicationTemplateRoute;
import cn.asany.ui.resources.dao.ComponentDao;
import cn.asany.ui.resources.domain.Component;
import cn.asany.ui.resources.domain.enums.ComponentScope;
import jakarta.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Service;

@Service
public class ApplicationTemplateService {

  private final ComponentDao componentDao;
  private final ApplicationTemplateDao applicationTemplateDao;
  private final ApplicationTemplateRouteDao applicationTemplateRouteDao;

  public ApplicationTemplateService(
      ApplicationTemplateDao applicationTemplateDao,
      ApplicationTemplateRouteDao applicationTemplateRouteDao,
      ComponentDao componentDao) {
    this.componentDao = componentDao;
    this.applicationTemplateDao = applicationTemplateDao;
    this.applicationTemplateRouteDao = applicationTemplateRouteDao;
  }

  public List<ApplicationTemplate> findAll() {
    return this.applicationTemplateDao.findAll();
  }

  @Transactional
  public void deleteApplicationTemplate(Long applicationId) {
    List<ApplicationTemplateRoute> routes =
        this.applicationTemplateRouteDao.findAll(
            PropertyFilter.newFilter()
                .equal("application.id", applicationId)
                .equal("component.scope", ComponentScope.ROUTE)
                .isNotNull("component"));
    Set<Long> componentIds =
        routes.stream().map(item -> item.getComponent().getId()).collect(Collectors.toSet());
    // 删除应用模版
    applicationTemplateDao.deleteById(applicationId);
    // 删除对应的路由组件
    this.componentDao.deleteAllById(componentIds);
  }

  @Transactional
  public ApplicationTemplate createApplicationTemplate(ApplicationTemplate application) {
    List<ApplicationTemplateRoute> routes = setupDefault(application.getRoutes());
    application.setRoutes(Collections.emptyList());
    this.applicationTemplateDao.save(application);
    application.setRoutes(routes);
    this.applicationTemplateRouteDao.saveAllInBatch(routes);
    return application;
  }

  @Transactional
  public ApplicationTemplate updateApplicationTemplate(ApplicationTemplate application) {
    ApplicationTemplate oldObject =
        this.applicationTemplateDao.getReferenceById(application.getId());
    ObjectUtil.copy(application, oldObject, "routes", "id");
    List<ApplicationTemplateRoute> routes = setupDefault(application.getRoutes());
    this.applicationTemplateDao.update(oldObject);
    oldObject.setRoutes(routes);
    return oldObject;
  }

  private List<ApplicationTemplateRoute> setupDefault(List<ApplicationTemplateRoute> routes) {
    setupDefault(routes, null);
    return ObjectUtil.flat(routes, "routes");
  }

  private void setupDefault(
      List<ApplicationTemplateRoute> routes, ApplicationTemplateRoute parent) {
    long index = 1;
    for (ApplicationTemplateRoute route : routes) {
      route.setLevel(parent == null ? 0 : parent.getLevel() + 1);
      route.setEnabled(Boolean.TRUE);
      route.setType(ObjectUtil.defaultValue(route.getType(), RouteType.ROUTE));
      route.setAuthorized(ObjectUtil.defaultValue(route.getAuthorized(), Boolean.FALSE));
      route.setHideInMenu(ObjectUtil.defaultValue(route.getHideInMenu(), Boolean.FALSE));
      route.setHideChildrenInMenu(
          ObjectUtil.defaultValue(route.getHideChildrenInMenu(), Boolean.FALSE));
      route.setHideInBreadcrumb(
          ObjectUtil.defaultValue(route.getHideInBreadcrumb(), Boolean.FALSE));
      route.setIndex(index++);

      Optional<Component> optionalComponent = setupComponent(route.getComponent());
      if (optionalComponent.isPresent()) {
        route.setComponent(optionalComponent.get());
      } else {
        route.setComponent(null);
      }

      if (route.getApplication() == null || route.getApplication().getId() == null) {
        System.out.println(route);
      }

      if (route.getRoutes() != null) {
        setupDefault(route.getRoutes(), route);
      }
    }
  }

  private Optional<Component> setupComponent(Component component) {
    if (component == null) {
      return Optional.empty();
    }
    if (component.getName() != null) {
      return this.componentDao.findOne(
          PropertyFilter.newFilter().equal("name", component.getName()));
    }
    if (component.getScope() != ComponentScope.ROUTE) {
      component.setScope(ComponentScope.ROUTE);
    }
    return Optional.of(this.componentDao.save(component));
  }

  public Optional<ApplicationTemplate> getApplicationTemplate(Long id) {
    return this.applicationTemplateDao.findById(id);
  }
}
