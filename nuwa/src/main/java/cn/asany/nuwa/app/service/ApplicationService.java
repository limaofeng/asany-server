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
package cn.asany.nuwa.app.service;

import cn.asany.base.IApplicationModule;
import cn.asany.base.IModuleProperties;
import cn.asany.base.ModuleConfig;
import cn.asany.base.ModuleResolver;
import cn.asany.nuwa.app.converter.ApplicationConverter;
import cn.asany.nuwa.app.dao.*;
import cn.asany.nuwa.app.domain.*;
import cn.asany.nuwa.app.domain.enums.ApplicationType;
import cn.asany.nuwa.app.domain.enums.MenuType;
import cn.asany.nuwa.app.domain.enums.RouteType;
import cn.asany.nuwa.app.service.dto.NativeApplication;
import cn.asany.nuwa.app.service.dto.NuwaMenu;
import cn.asany.nuwa.app.service.dto.NuwaRoute;
import cn.asany.nuwa.app.service.dto.OAuthApplication;
import cn.asany.nuwa.template.dao.ApplicationTemplateDao;
import cn.asany.nuwa.template.domain.ApplicationTemplate;
import cn.asany.nuwa.template.domain.ApplicationTemplateMenu;
import cn.asany.nuwa.template.domain.ApplicationTemplateRoute;
import cn.asany.security.core.domain.Tenant;
import cn.asany.security.core.service.TenantService;
import cn.asany.ui.resources.dao.ComponentDao;
import cn.asany.ui.resources.domain.Component;
import cn.asany.ui.resources.domain.enums.ComponentScope;
import java.util.*;
import java.util.stream.Collectors;
import net.asany.jfantasy.framework.dao.hibernate.util.HibernateUtils;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.error.ValidationException;
import net.asany.jfantasy.framework.security.auth.core.ClientDetails;
import net.asany.jfantasy.framework.security.auth.core.ClientDetailsService;
import net.asany.jfantasy.framework.security.auth.core.ClientRegistrationException;
import net.asany.jfantasy.framework.security.auth.core.ClientSecretType;
import net.asany.jfantasy.framework.util.common.ClassUtil;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 应用服务
 *
 * @author limaofeng
 */
@Service
public class ApplicationService implements ClientDetailsService {

  public static final String NONCE_CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";
  public static final String CACHE_KEY = "NUWA";

  private final ApplicationDao applicationDao;
  private final ApplicationDependencyDao applicationDependencyDao;
  private final ClientSecretDao clientSecretDao;
  private final ApplicationRouteDao applicationRouteDao;
  private final ApplicationMenuDao applicationMenuDao;
  private final ApplicationTemplateDao applicationTemplateDao;

  private final ApplicationModuleConfigurationDao applicationModuleConfigurationDao;
  private final ComponentDao componentDao;
  private final ApplicationConverter applicationConverter;
  private final ModuleResolver moduleResolver;
  private final CacheManager cacheManager;
  private final TenantService tenantService;

  public ApplicationService(
      CacheManager cacheManager,
      TenantService tenantService,
      ApplicationDao applicationDao,
      ApplicationDependencyDao applicationDependencyDao,
      ClientSecretDao clientSecretDao,
      ApplicationModuleConfigurationDao applicationModuleConfigurationDao,
      ComponentDao componentDao,
      ApplicationConverter applicationConverter,
      ApplicationRouteDao applicationRouteDao,
      ApplicationMenuDao applicationMenuDao,
      ApplicationTemplateDao applicationTemplateDao,
      ModuleResolver moduleResolver) {
    this.tenantService = tenantService;
    this.applicationDao = applicationDao;
    this.clientSecretDao = clientSecretDao;
    this.applicationModuleConfigurationDao = applicationModuleConfigurationDao;
    this.componentDao = componentDao;
    this.applicationConverter = applicationConverter;
    this.applicationRouteDao = applicationRouteDao;
    this.applicationMenuDao = applicationMenuDao;
    this.applicationTemplateDao = applicationTemplateDao;
    this.applicationDependencyDao = applicationDependencyDao;
    this.moduleResolver = moduleResolver;
    this.cacheManager = cacheManager;
  }

  public List<Application> findAll(PropertyFilter filter) {
    return applicationDao.findAll(filter);
  }

  @Override
  @Cacheable(key = "targetClass + '.' +  methodName + '#' + #p0", value = CACHE_KEY)
  public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
    Optional<Application> optional =
        this.applicationDao.findOneWithClientDetails(
            PropertyFilter.newFilter().equal("clientId", clientId).equal("enabled", true));
    if (optional.isEmpty()) {
      throw new ClientRegistrationException("[client_id=" + clientId + "]不存在");
    }
    return optional.get();
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public boolean existsByClientId(String clientId) {
    return this.applicationDao.exists(PropertyFilter.newFilter().equal("clientId", clientId));
  }

  @Transactional(rollbackFor = RuntimeException.class)
  @Cacheable(
      key = "targetClass  + '.' +  methodName + '#' + #p0",
      value = CACHE_KEY,
      condition = "#p1 && #p2")
  public Optional<Application> findDetailsByClientId(
      String id, boolean hasFetchRoutes, boolean hasFetchMenus) {
    Optional<Application> optional;
    if (hasFetchRoutes && hasFetchMenus) {
      optional = this.applicationDao.findDetailsByClientId(id);
    } else if (hasFetchRoutes) {
      optional = this.applicationDao.findOneWithRoutesByClientId(id);
    } else if (hasFetchMenus) {
      optional = this.applicationDao.findOneWithMenusByClientId(id);
    } else {
      optional = this.applicationDao.findOneBy("clientId", id);
    }
    return optional.map(HibernateUtils::cloneEntity);
  }

  @Transactional(readOnly = true)
  @Cacheable(key = "targetClass  + '.' +  methodName + '#' + #p0", value = CACHE_KEY)
  public List<ApplicationDependency> findDependencies(Long id) {
    return this.applicationDependencyDao.findAll(
        PropertyFilter.newFilter().equal("application.id", id), Sort.by("createdAt").ascending());
  }

  @Transactional(rollbackFor = RuntimeException.class)
  @Cacheable(
      key = "targetClass + '.' + methodName + '#' + #p0",
      value = CACHE_KEY,
      condition = "#p1 && #p2")
  public Optional<Application> findDetailsById(
      Long id, boolean hasFetchRoutes, boolean hasFetchMenus) {
    if (hasFetchRoutes && hasFetchMenus) {
      return this.applicationDao.findDetailsById(id);
    }
    if (hasFetchRoutes) {
      return this.applicationDao.findOneWithRoutesById(id);
    }
    if (hasFetchMenus) {
      return this.applicationDao.findOneWithMenusById(id);
    }
    return this.applicationDao.findById(id);
  }

  @Transactional
  public List<ApplicationRoute> findRouteAllByApplicationWithComponent(Long applicationId) {
    return this.applicationRouteDao.findAllByApplicationWithComponent(applicationId);
  }

  @Transactional(rollbackFor = RuntimeException.class)
  public Application createApplication(OAuthApplication app) {
    Application application = this.applicationConverter.oauthAppToApp(app);
    String clientId = StringUtil.generateNonceString(NONCE_CHARS, 20);
    String clientSecret = StringUtil.generateNonceString(NONCE_CHARS, 40);
    Set<ClientSecret> clientSecrets = new HashSet<>();
    clientSecrets.add(ClientSecret.builder().id(1L).client(clientId).secret(clientSecret).build());
    application.setClientId(clientId);
    application.setClientSecretsAlias(clientSecrets);
    return application;
  }

  @Transactional
  public Application createApplication(NativeApplication nativeApplication) {
    return createApplication(nativeApplication, Optional.empty());
  }

  @Transactional
  public Application createApplication(NativeApplication nativeApplication, Long template) {
    Optional<ApplicationTemplate> optionalTemplate = this.applicationTemplateDao.findById(template);
    if (optionalTemplate.isEmpty()) {
      throw new ValidationException("应用模版" + template + "不存在");
    }
    return createApplication(nativeApplication, optionalTemplate);
  }

  public Application createApplication(
      NativeApplication nativeApplication, Optional<ApplicationTemplate> template) {
    String clientId =
        ObjectUtil.defaultValue(
            nativeApplication.getClientId(), () -> StringUtil.generateNonceString(NONCE_CHARS, 20));
    String clientSecretStr =
        ObjectUtil.defaultValue(
            nativeApplication.getClientSecret(),
            () -> StringUtil.generateNonceString(NONCE_CHARS, 40));

    // 创建密钥
    Set<ClientSecret> clientSecrets = new HashSet<>();
    ClientSecret clientSecret =
        clientSecretDao.save(
            ClientSecret.builder()
                .client(clientId)
                .secret(clientSecretStr)
                .type(ClientSecretType.OAUTH)
                .build());
    clientSecrets.add(clientSecret);

    Tenant tenant =
        this.tenantService
            .findById("1691832353955123200")
            .orElseThrow(() -> new ValidationException("租户不存在"));

    // 创建应用
    Application application =
        Application.builder()
            .type(ObjectUtil.defaultValue(nativeApplication.getType(), ApplicationType.Native))
            .name(nativeApplication.getName())
            .description(nativeApplication.getDescription())
            .clientId(clientId)
            .clientSecretsAlias(clientSecrets)
            .tenantId(tenant.getId())
            .routes(new HashSet<>())
            .build();

    // 生成应用路由
    ApplicationTemplate applicationTemplate = template.orElse(null);
    if (nativeApplication.getRoutes() != null && !nativeApplication.getRoutes().isEmpty()) {
      application.setRoutes(getRoutesFromNuwa(nativeApplication.getRoutes()));
    } else if (applicationTemplate != null) {
      application.setRoutes(getRoutesFromTemplate(applicationTemplate));
    }

    // 生成菜单
    if (nativeApplication.getMenus() != null && !nativeApplication.getMenus().isEmpty()) {
      application.setMenus(getMenusFromNuwa(nativeApplication.getMenus()));
    } else if (applicationTemplate != null) {
      application.setMenus(getMenusFromTemplate(applicationTemplate));
    }

    // 保存应用
    this.applicationDao.save(application);

    Set<ApplicationModuleConfiguration> modules = new HashSet<>();
    List<IModuleProperties> modulePropertiesList =
        ObjectUtil.defaultValue(nativeApplication.getModules(), Collections.emptyList());
    for (IModuleProperties moduleProperties : modulePropertiesList) {
      IApplicationModule<IModuleProperties> module =
          this.moduleResolver.resolve(moduleProperties.getType());
      ApplicationModuleConfiguration moduleConfiguration =
          ApplicationModuleConfiguration.builder()
              .module(ApplicationModule.builder().id(moduleProperties.getType()).build())
              .application(application)
              .values(
                  module.install(
                      ModuleConfig.builder()
                          .tenant(tenant.getId())
                          .defaultOrganization(tenant.getDefaultOrganization().getId())
                          .properties(moduleProperties)
                          .build()))
              .build();
      modules.add(this.applicationModuleConfigurationDao.save(moduleConfiguration));
    }
    application.setModules(modules);

    return application;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteApplication(String clientId) {
    Optional<Application> application =
        this.applicationDao.findOne(PropertyFilter.newFilter().equal("clientId", clientId));
    if (application.isEmpty()) {
      return;
    }
    deleteApplication(application.get().getId());
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteApplication(Long id) {
    Application app = this.applicationDao.getReferenceById(id);

    Tenant tenant =
        this.tenantService
            .findById(app.getTenantId())
            .orElseThrow(() -> new ValidationException("租户不存在"));

    // 路由组件
    List<ApplicationRoute> routes =
        this.applicationRouteDao.findAll(
            PropertyFilter.newFilter()
                .equal("component.scope", ComponentScope.ROUTE)
                .equal("application.id", id)
                .isNotNull("component"));
    List<Component> components =
        routes.stream().map(ApplicationRoute::getComponent).collect(Collectors.toList());

    // 菜单组件
    List<ApplicationMenu> menus =
        this.applicationMenuDao.findAll(
            PropertyFilter.newFilter()
                .equal("component.scope", ComponentScope.MENU)
                .equal("application.id", id)
                .isNotNull("component"));
    components.addAll(menus.stream().map(ApplicationMenu::getComponent).toList());

    String clientId = app.getClientId();

    for (ApplicationModuleConfiguration config : app.getModules()) {
      IApplicationModule<IModuleProperties> module =
          moduleResolver.resolve(config.getModule().getId());
      Class<IModuleProperties> propertiesType =
          ClassUtil.getInterfaceGenricType(module.getClass(), IApplicationModule.class);

      ModuleConfig<IModuleProperties> moduleConfig =
          ModuleConfig.builder()
              .tenant(tenant.getId())
              .defaultOrganization(tenant.getDefaultOrganization().getId())
              .properties(
                  ClassUtil.newInstance(
                      propertiesType,
                      new Class<?>[] {Map.class},
                      new Object[] {config.getValues()}))
              .build();

      module.uninstall(moduleConfig);
    }

    // 删除应用
    this.applicationDao.deleteById(id);

    // 删除对应的组件
    this.componentDao.deleteAll(components);

    Cache cache = this.cacheManager.getCache(CACHE_KEY);
    assert cache != null;
    cache.evictIfPresent(ApplicationService.class + ".loadClientByClientId#" + clientId);
    cache.evictIfPresent(ApplicationService.class + ".findDetailsByClientId#" + clientId);
    cache.evictIfPresent(ApplicationService.class + ".findDetailsById#" + id);
  }

  private Set<ApplicationMenu> getMenusFromNuwa(List<NuwaMenu> nuwaMenus) {
    List<Component> components = new ArrayList<>();
    List<ApplicationMenu> menus =
        ObjectUtil.recursive(
            nuwaMenus,
            (item, context) -> {
              ApplicationMenu menu = this.applicationConverter.toMenuFromNuwa(item);

              menu.setIndex(context.getIndex());
              menu.setLevel(context.getLevel());

              if (menu.getType() == null) {
                // 这里使用的是 item， 因为子项在之后才会执行
                if (item.getChildren() != null
                    && !item.getChildren().isEmpty()
                    && !menu.getHideChildrenInMenu()) {
                  menu.setType(MenuType.MENU);
                } else {
                  menu.setType(MenuType.URL);
                }
              }

              Component component = menu.getComponent();
              if (context.getLevel() != 1 && component != null) {
                menu.setComponent(component = null);
              }
              if (component == null) {
                return menu;
              }
              component.setScope(ComponentScope.MENU);
              components.add(component);

              return menu;
            });
    this.componentDao.saveAllInBatch(components);
    menus = ObjectUtil.flat(menus, "children");
    return new HashSet<>(menus);
  }

  private Set<ApplicationMenu> getMenusFromTemplate(ApplicationTemplate applicationTemplate) {
    List<ApplicationTemplateMenu> templateMenus =
        ObjectUtil.tree(applicationTemplate.getMenus(), "id", "parent.id", "children");
    List<ApplicationMenu> menus = applicationConverter.toMenusFromTemplate(templateMenus);
    return new HashSet<>(ObjectUtil.flat(menus, "children"));
  }

  private Set<ApplicationRoute> getRoutesFromNuwa(List<NuwaRoute> nuwaRoutes) {
    List<Component> components = new ArrayList<>();
    List<ApplicationRoute> routes =
        ObjectUtil.recursive(
            nuwaRoutes,
            (item, context) -> {
              ApplicationRoute route = this.applicationConverter.toRouteFromNuwa(item);
              route.setIndex(context.getIndex());
              route.setLevel(context.getLevel());
              route.setType(RouteType.ROUTE);
              route.setEnabled(ObjectUtil.defaultValue(route.getEnabled(), true));
              route.setAuthorized(ObjectUtil.defaultValue(route.getAuthorized(), false));
              route.setLayout(
                  ObjectUtil.defaultValue(
                      route.getLayout(), () -> LayoutSettings.builder().build()));
              route.setHideInBreadcrumb(false);
              Component component = route.getComponent();
              if (component == null) {
                return route;
              }
              component.setScope(ComponentScope.ROUTE);
              components.add(component);
              return route;
            },
            "routes");
    this.componentDao.saveAllInBatch(components);
    return new HashSet<>(ObjectUtil.flat(routes, "routes"));
  }

  private Set<ApplicationRoute> getRoutesFromTemplate(ApplicationTemplate applicationTemplate) {
    List<ApplicationTemplateRoute> templateRoutes =
        ObjectUtil.tree(
            applicationTemplate.getRoutes(),
            "id",
            "parent.id",
            "routes",
            item -> {
              ApplicationTemplateRoute route =
                  ObjectUtil.clone(item, "routes", "application", "component");
              Component component = item.getComponent();
              if (component == null) {
                return route;
              }
              if (component.getScope() == ComponentScope.ROUTE) {
                route.setComponent(this.componentDao.save(ObjectUtil.clone(component, "id")));
              } else {
                route.setComponent(Component.builder().id(component.getId()).build());
              }
              return route;
            });
    List<ApplicationRoute> routes = applicationConverter.toRoutes(templateRoutes);
    return new HashSet<>(ObjectUtil.flat(routes, "routes"));
  }

  public Optional<ApplicationRoute> getRoute(Long id) {
    return this.applicationRouteDao.findById(id);
  }

  public void addRoute(ApplicationRoute route) {
    this.applicationRouteDao.save(route);
    Cache cache = this.cacheManager.getCache(CACHE_KEY);
    assert cache != null;
    Long appId = route.getApplication().getId();
    String clientId = route.getApplication().getClientId();
    cache.evictIfPresent(ApplicationService.class + ".loadClientByClientId#" + clientId);
    cache.evictIfPresent(ApplicationService.class + ".findDetailsByClientId#" + clientId);
    cache.evictIfPresent(ApplicationService.class + ".findDetailsById#" + appId);
  }

  public void clearAppCache(Long appId) {
    clearApplication(this.applicationDao.getReferenceById(appId));
  }

  public void clearApplication(Application application) {
    Cache cache = this.cacheManager.getCache(CACHE_KEY);
    assert cache != null;
    Long appId = application.getId();
    String clientId = application.getClientId();
    cache.evictIfPresent(ApplicationService.class + ".loadClientByClientId#" + clientId);
    cache.evictIfPresent(ApplicationService.class + ".findDetailsByClientId#" + clientId);
    cache.evictIfPresent(ApplicationService.class + ".findDetailsById#" + appId);
  }
}
